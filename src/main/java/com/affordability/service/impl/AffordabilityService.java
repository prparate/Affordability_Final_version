package com.affordability.service.impl;

import com.affordability.customer.decision.ICustomerDecision;
import com.affordability.customer.funding.IFundedCustomer;
import com.affordability.customer.regulation.CustomerDataForDetermineRegulation;
import com.affordability.customer.regulation.ICustomerDataForRegulationProvider;
import com.affordability.customer.regulation.IRegulatedCustomerIdentifier;
import com.affordability.customer.segmentation.CustomerInfo;
import com.affordability.customer.segmentation.ICustomerSegmentationAssigner;
import com.affordability.dto.request.AffordabilityVerification;
import com.affordability.dto.response.AgreementDetail;
import com.affordability.dto.response.AgreementLastRecordResponse;
import com.affordability.exception.AgreementRecentlyVerifiedException;
import com.affordability.exception.ApplicationException;
import com.affordability.exception.CustomerNotFoundException;
import com.affordability.exception.MicroservicesConnectionException;
import com.affordability.model.AffordabilitySummary;
import com.affordability.persistence.IAffordabilityOutcomeRepository;
import com.affordability.persistence.IErrorCraUpdateRepository;
import com.affordability.persistence.jpa.AffordabilityOutcomeJPA;
import com.affordability.service.IAffordabilityService;
import com.affordability.service.cra.ICraService;
import com.affordability.service.cra.request.CraRequest;
import com.affordability.service.cra.response.DelphiResponse;
import com.affordability.service.datafetcher.IAgreementSummaryService;
import com.affordability.service.datafetcher.IPartnerDetailsForCommercialAgreement;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response.PartnerDetailsForCommercialAgreementQueryResponse;
import com.affordability.service.process.IProcessValidator;
import com.affordability.thread.UpdateCraThread;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;

import java.util.UUID;

import static com.affordability.model.EActivityType.fromTextToValue;
import static com.affordability.model.EBusinessLine.COMMERCIAL;
import static com.affordability.model.EBusinessLine.PERSONAL;
import static com.affordability.utils.EnumUtil.getCustomerTypeString;
import static com.affordability.utils.EnumUtil.getEnumName;
import static java.lang.String.format;

@Service
@Slf4j
@RequiredArgsConstructor
public class AffordabilityService implements IAffordabilityService {

    private final IAgreementSummaryService agreementSummaryService;
    private final ICustomerSegmentationAssigner customerSegmentationAssigner;
    private final ICraService craService;
    private final IAffordabilityOutcomeRepository affordabilityOutcomeRepository;
    private final IRegulatedCustomerIdentifier regulatedCustomerIdentifier;
    private final ICustomerDataForRegulationProvider customerDataForRegulationProvider;
    private final ICustomerDecision customerDecision;
    private final IFundedCustomer fundedCustomer;
    private final IPartnerDetailsForCommercialAgreement partnerDetailsForCommercialAgreement;
    private final IProcessValidator processController;

    private final IErrorCraUpdateRepository errorCraUpdateRepository;

    @Qualifier("retryTemplateCraUpdate")
    private final RetryTemplate retryTemplate;
    private final RestTemplate restTemplate;


    @Value("${cra.base-url}/v1/client-agreement/affordability-schema")
    private String updateCraUrl;

    private static final String CRA_NOT_AVAILABLE = "CRA not available";
    private static final String CRA_NO_MATCH = "No customer found";

    @Override
    public AffordabilitySummary verify(AffordabilityVerification affordabilityVerification) throws Exception {
        final var agreementNumber = affordabilityVerification.getAgreementNumber();
        final var environment = affordabilityVerification.getEnvironment();
        final var activityType = fromTextToValue(affordabilityVerification.getTransactionActivity());
        final var affordabilitySummary = new AffordabilitySummary(agreementNumber, activityType, environment);

        try {
            processController.checkVerificationProcessStarted(agreementNumber);

            var lastOutcome = affordabilityOutcomeRepository.findLastOutcome(agreementNumber);
            lastOutcome.ifPresent(outcomeJPA -> affordabilitySummary
                .addLastOutcome(outcomeJPA.getAffordabilitySummaryJsonData()));

            final boolean isFunded = fundedCustomer.isFundedCustomer(environment);


            if (!isFunded) {
                log.info("Customer Agreement {} is non-funded", agreementNumber);
                affordabilitySummary.markAsUnFunded();
                return createAffordabilitySummary(affordabilitySummary);
            }

            affordabilitySummary.addFunded(isFunded);
            log.info("Customer Agreement {} is funded", agreementNumber);
            var customerRegulationData = getCustomerRegulationData(agreementNumber);
            var isRegulatedCustomer = isRegulatedCustomer(customerRegulationData);

            var businessLine = customerRegulationData.getBusinessLine();

            affordabilitySummary.setInclusion(isRegulatedCustomer);
            affordabilitySummary.addNumberOfPatterns(customerRegulationData.getNumberOfPartners(), businessLine);
            affordabilitySummary.addIncorporationStatus(customerRegulationData.getIncorporationStatus());

            CraRequest craRequest = null;

            final AgreementDetail agreementDetails = agreementSummaryService.getAgreementSummary(agreementNumber);
            log.info("Customer Agreement {} Business line : {}", agreementNumber, businessLine);

            affordabilitySummary.addAgreementDetails(agreementDetails);
            if (COMMERCIAL.equals(businessLine)) {
                var partner = getFirstPartnerDetailsForCommercialAgreement(agreementNumber);
                affordabilitySummary.addCommercialInfo(agreementDetails, partner, customerRegulationData.getTraderType());
                craRequest = getCraRequestCommercialData(partner, agreementDetails);
            }

            if (PERSONAL.equals(businessLine)) {
                affordabilitySummary.addPersonalInfo(agreementDetails, customerRegulationData.getTraderType());
                craRequest = getCraRequestPersonalData(agreementDetails);
            }

            if (!isRegulatedCustomer) {
                log.info("Customer Agreement {} is non-regulated", agreementNumber);
                return createAffordabilitySummary(affordabilitySummary);
            }

            log.info("Customer Agreement {} is regulated", agreementNumber);

            DelphiResponse delphiData = null;

            delphiData = craService.getDelphiData(craRequest);
            affordabilitySummary.addDelphiData(delphiData);

            var customerInfo = new CustomerInfo();
            customerInfo.setDelphiBlock(delphiData.getData());

            var assignedSegment = customerSegmentationAssigner.determineSegment(customerInfo);
            var customerSegment = assignedSegment.getIdentifier();
            log.info("Customer Agreement {} is assigned to segment : {}", agreementNumber, customerSegment);

            affordabilitySummary.addCustomerSegment(customerSegment);

            var decisionOutcome = customerDecision.processCustomerSegment(customerSegment, delphiData.getData(), craRequest, agreementDetails);

            var customerAffordabilityStatus = decisionOutcome.status;

            var effectiveDisposableSurplus = decisionOutcome.effectiveDisposableSurplus;

            var lowInstalment = decisionOutcome.lowInstalment;
            var incomeExpenditure = decisionOutcome.incomeExpenditure;

            affordabilitySummary.confirmOutcome(customerDecision.getConsumerSummary(), customerAffordabilityStatus, effectiveDisposableSurplus, lowInstalment, incomeExpenditure);

            return createAffordabilitySummary(affordabilitySummary);

        } catch (CustomerNotFoundException e) {
            saveDomainModelError(affordabilitySummary, CRA_NO_MATCH);
            throw new ApplicationException(agreementNumber, e, HttpStatus.BAD_REQUEST);
        } catch (MicroservicesConnectionException | RestClientException e) {
            saveDomainModelError(affordabilitySummary, CRA_NOT_AVAILABLE);
            var message = "Error to communicate with premfina microservices";
            throw new ApplicationException(agreementNumber, e, HttpStatus.BAD_REQUEST, message);
        } catch (NotFoundException e) {
            throw new ApplicationException(agreementNumber, e, HttpStatus.BAD_REQUEST);
        } catch (AgreementRecentlyVerifiedException e) {
            throw new ApplicationException(agreementNumber, e, HttpStatus.CONFLICT);
        } catch (Exception e) {
            log.error("here");
            saveDomainModelError(affordabilitySummary, e.getMessage());
            throw new ApplicationException(agreementNumber, e);
        }
    }

    private void saveDomainModelError(AffordabilitySummary affordabilitySummary, String messageError) {
        affordabilitySummary.markAsError(messageError);
        this.persistAffordabilitySummary(affordabilitySummary);
    }

    private CraRequest getCraRequestPersonalData(AgreementDetail agreementDetail) {
        var craRequest = new CraRequest();

        craRequest.agreementNumber = agreementDetail.getAgrAgreementNumber();
        craRequest.personDateOfBirth = agreementDetail.getPersonDateOfBirth();
        craRequest.personTitle = agreementDetail.getPersonTitle();
        craRequest.personNameFirst = agreementDetail.getPersonNameFirst();
        craRequest.personNameLast = agreementDetail.getPersonNameLast();
        craRequest.personNameMiddle = agreementDetail.getPersonNameMiddle();

        craRequest.agrAdvance = agreementDetail.getAgrAdvance();
        craRequest.bankAccountNumber = agreementDetail.getBankAccountNumber();
        craRequest.bankAccountSortCode = agreementDetail.getBankAccountSortCode();

        craRequest.personHouseNumber = agreementDetail.getPersonHouseNumber();
        craRequest.personHouseName = agreementDetail.getPersonHouseName();
        craRequest.personStreetName = agreementDetail.getPersonStreetName();
        craRequest.personStreetType = agreementDetail.getPersonStreetType();
        craRequest.personLocality = agreementDetail.getPersonLocality();
        craRequest.personPostCode = agreementDetail.getPersonPostCode();
        craRequest.personPostTown = agreementDetail.getPersonPostTown();
        craRequest.personCountyCode = agreementDetail.getPersonCountyCode();
        craRequest.personCountry = agreementDetail.getPersonCountry();

        craRequest.habitationDateIn = agreementDetail.getPersonHabitationDateIn();
        craRequest.habitationDateOut = agreementDetail.getPersonHabitationDateOut();

        return craRequest;
    }

    private CraRequest getCraRequestCommercialData(PartnerDetailsForCommercialAgreementQueryResponse firstPartnerDetails, AgreementDetail agreementDetail) {
        var craRequest = new CraRequest();

        craRequest.agreementNumber = agreementDetail.getAgrAgreementNumber();
        craRequest.personDateOfBirth = firstPartnerDetails.getDateOfBirth();
        craRequest.personTitle = firstPartnerDetails.getPersonTitle();
        craRequest.personNameFirst = firstPartnerDetails.getPersonForenames();
        craRequest.personNameLast = firstPartnerDetails.getPersonSurname();
        craRequest.personNameMiddle = firstPartnerDetails.getPersonMiddleName();

        craRequest.agrAdvance = agreementDetail.getAgrAdvance();
        craRequest.bankAccountNumber = agreementDetail.getBankAccountNumber();
        craRequest.bankAccountSortCode = agreementDetail.getBankAccountSortCode();

        craRequest.personHouseNumber = firstPartnerDetails.getHouseNumber();
        craRequest.personHouseName = firstPartnerDetails.getHouseName();
        craRequest.personStreetName = firstPartnerDetails.getStreetName();
        craRequest.personStreetType = firstPartnerDetails.getStreetType();
        craRequest.personLocality = firstPartnerDetails.getLocality();
        craRequest.personPostCode = firstPartnerDetails.getPostalCode();
        craRequest.personPostTown = firstPartnerDetails.getPostTown();
        craRequest.personCountyCode = firstPartnerDetails.getCountyCode();
        craRequest.personCountry = firstPartnerDetails.getCountry();

        craRequest.habitationDateIn = firstPartnerDetails.getHabitationDateIn();
        craRequest.habitationDateOut = firstPartnerDetails.getHabitationDateOut();

        return craRequest;
    }

    private AffordabilitySummary createAffordabilitySummary(AffordabilitySummary affordabilitySummary) {

        var outcomeId = this.persistAffordabilitySummary(affordabilitySummary);

        this.updateCra(outcomeId, affordabilitySummary);

        return affordabilitySummary;
    }

    private boolean isRegulatedCustomer(CustomerDataForDetermineRegulation data) {
        this.regulatedCustomerIdentifier.setCustomerDataForRegulation(data);
        return this.regulatedCustomerIdentifier.isThisARegulatedCustomer();
    }

    private CustomerDataForDetermineRegulation getCustomerRegulationData(String agreementNumber) {
        return this.customerDataForRegulationProvider.findData(agreementNumber);
    }

    private UUID persistAffordabilitySummary(AffordabilitySummary affordabilitySummary) {
        var entity = affordabilityOutcomeRepository.save(
            AffordabilityOutcomeJPA.builder()
                .agreementNumber(affordabilitySummary.agreementNumber)
                .affordabilitySummaryJsonData(affordabilitySummary)
                .environment(affordabilitySummary.environment)
                .createdOn(affordabilitySummary.createdOn)
                .finalCreatedOn(affordabilitySummary.finalCreatedOn)
                .customerType(getCustomerTypeString(affordabilitySummary.customer))
                .status(getEnumName(affordabilitySummary.status))
                .segmentation(getEnumName(affordabilitySummary.customerSegment))
                .decisionType(getEnumName(affordabilitySummary.finalDecision))
                .activityType(getEnumName(affordabilitySummary.activityType))
                .exclusionReason(getEnumName(affordabilitySummary.exclusionReason))
                .finalDecision(getEnumName(affordabilitySummary.finalDecision))
                .inclusionOutcome(getEnumName(affordabilitySummary.inclusionOutcome))
                .automatedDecision(getEnumName(affordabilitySummary.automatedDecision))
                .build()
        );

        return entity.getId();
    }

    private PartnerDetailsForCommercialAgreementQueryResponse getFirstPartnerDetailsForCommercialAgreement(String agreementNumber) {

        return partnerDetailsForCommercialAgreement.getFirstPartnerDetailsForCommercialAgreement(agreementNumber);
    }

    private void updateCra(UUID outcomeId, AffordabilitySummary affordabilitySummary) {

        if (!affordabilitySummary.isAllowedToUpdateCra()) {
            log.info("Agreement: %s is not allowed to update CRA".formatted(affordabilitySummary.agreementNumber));
            return;
        }

        new UpdateCraThread(retryTemplate, restTemplate, outcomeId, affordabilitySummary, errorCraUpdateRepository,
            updateCraUrl)
            .start();
    }

    public AgreementLastRecordResponse getLastOutcome(String agreementNumber, String environment) throws ApplicationException {
        var lastOutcome = affordabilityOutcomeRepository.findLastOutcome(agreementNumber, environment);

        if (lastOutcome.isEmpty()) {
            String message = format("There is no outcome for this agreement number at %s environment", environment);
            throw new ApplicationException(agreementNumber, message, HttpStatus.NOT_FOUND);
        }

        return lastOutcome.get();
    }

}
