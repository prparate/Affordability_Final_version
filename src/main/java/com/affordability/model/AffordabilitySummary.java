package com.affordability.model;

import com.affordability.customer.ETraderType;
import com.affordability.customer.segmentation.ECustomerSegment;
import com.affordability.dto.response.AgreementDetail;
import com.affordability.service.cra.response.ConsumerSummaryResponse;
import com.affordability.service.cra.response.DataSegmentResponse;
import com.affordability.service.cra.response.DelphiResponse;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response.PartnerDetailsForCommercialAgreementQueryResponse;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.NoArgsConstructor;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import static com.affordability.customer.segmentation.ECustomerSegment.NOT_CLASSIFIED;
import static com.affordability.model.EAutomatedDecision.*;
import static com.affordability.model.EStatus.ON_SCHEME;
import static java.time.Period.between;
import static java.time.format.DateTimeFormatter.ofPattern;


@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@NoArgsConstructor
public final class AffordabilitySummary implements Serializable {

    public String agreementNumber;
    public EActivityType activityType;
    public Customer customer;
    public String environment;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    public Instant createdOn;
    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    public Instant agreementCreatedOn;
    public AffordabilityDelphi affordabilityDelphi;
    public ConsumerSummaryDelphi consumerSummaryDelphi;
    public ECustomerSegment customerSegment;
    public EInclusionOutcome inclusionOutcome;
    public EExclusionReason exclusionReason;
    public EStatus status;
    public EStatus statusBefore;
    public EAutomatedDecision automatedDecision;

    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    public Instant automatedDecisionDate;

    public ESchemaAssigned finalSchemeAssigned;
    public ESchemaAssigned automatedSchemeAssigned;
    public EAutomatedDecision finalDecision;
    public boolean isFunded;
    public boolean isRegulated;
    public BigDecimal totalPremiumValue;
    public String effectiveDisposableSurplus;
    public ELowInstalment lowInstalment;
    public String incomeExpenditure;
    public String reason;
    public Integer agreementSerial;
    @JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", timezone = "UTC")
    public Instant finalCreatedOn;
    public BigDecimal financeInstalmentAmount;

    public EIncorporationStatus incorporationStatus;

    public static final String DATE_FORMAT = "yyyy-MM-dd HH:mm:ss";

    @JsonIgnore
    public AffordabilitySummary(String agreementNumber, EActivityType activityType, String environment) {
        this.agreementNumber = agreementNumber;
        this.activityType = activityType;
        this.environment = environment;
        this.createdOn = Instant.now();
        this.finalCreatedOn = Instant.now();
        this.customer = new Customer();
        this.customer.isNew = EActivityType.NEW.equals(activityType);
    }

    public void addAgreementDetails(AgreementDetail agreementDetails) {
        if (agreementDetails != null) {
            this.totalPremiumValue = agreementDetails.getAgrAdvance();
            this.agreementCreatedOn = convertStringToInstant(agreementDetails.getAgrCreateDate(), DATE_FORMAT);
            this.agreementSerial = agreementDetails.getAgrSerial();
            this.financeInstalmentAmount = agreementDetails.getAgrInstalment();
        }
    }

    @JsonIgnore
    public void addFunded(Boolean isFunded) {
        this.isFunded = isFunded;
    }

    @JsonIgnore
    public void setInclusion(boolean isRegulated) {
        this.isRegulated = isRegulated;
        if (isRegulated) {
            this.inclusionOutcome = EInclusionOutcome.INCLUSION;
        } else {
            this.exclusionReason = EExclusionReason.UN_REGULATED;
            this.inclusionOutcome = EInclusionOutcome.EXCLUSION;
        }
    }

    public void addNumberOfPatterns(Integer numberOfPartners, EBusinessLine businessLine) {
        if (customer != null && businessLine.equals(EBusinessLine.COMMERCIAL)) {
            customer.numberOfPartners = String.valueOf(numberOfPartners);
        }
    }

    public void addDelphiData(DelphiResponse delphiResponse){
        this.affordabilityDelphi = delphiResponse != null ? convertToAffordabilityDelphi(delphiResponse) : null;
    }

    public void addCustomerSegment(ECustomerSegment assignedSegment){
        this.customerSegment = assignedSegment;
    }

    @JsonIgnore
    public void confirmOutcome( ConsumerSummaryResponse consumerSummary, EStatus status,
                                String effectiveDisposableSurplus,ELowInstalment lowInstalment,
                                String incomeExpenditure) {

        addDecision(status);
        this.consumerSummaryDelphi = convertToConsumerSummaryDelphi(consumerSummary);
        this.effectiveDisposableSurplus = effectiveDisposableSurplus;
        this.lowInstalment = lowInstalment;
        this.incomeExpenditure = incomeExpenditure;
    }

    private AffordabilityDelphi convertToAffordabilityDelphi(DelphiResponse delphiResponse) {
        var data = delphiResponse.getData();

        if (data == null) {
            return null;
        }

        return AffordabilityDelphi.builder()
                .requestedOn(convertStringToInstant(delphiResponse.getDatetimeRequest(), DATE_FORMAT))
                .uuidInstance(data.getUuid())
                .ndspcii(getData(data.getNdspcii()))
                .e1b07(getData(data.getE1b07()))
                .e1b08(getData(data.getE1b08()))
                .ea1c01(getData(data.getEa1c01()))
                .ea4q05(getData(data.getEa4q05()))
                .e1a07(getData(data.getE1a07()))
                .e1d02(getData(data.getE1d02()))
                .e1b09(getData(data.getE1b09()))
                .ndecc03(getData(data.getNdecc03()))
                .build();
    }

    private String getData(DataSegmentResponse data) {
        return Optional.ofNullable(data)
                .map(DataSegmentResponse::getValue)
                .orElse(null);
    }

    private ConsumerSummaryDelphi convertToConsumerSummaryDelphi(ConsumerSummaryResponse delphiResponse) {
        var isNotClassifiedWithInvalidCreditData = NOT_CLASSIFIED.equals(this.customerSegment)
            && this.affordabilityDelphi != null
            && this.affordabilityDelphi.getNdecc03().equals("-1")
            && this.affordabilityDelphi.getE1b09().equals("-1");

        if (isNotClassifiedWithInvalidCreditData || delphiResponse == null) {
            return null;
        }

        var data = delphiResponse.getData();

        return ConsumerSummaryDelphi.builder()
                .requestedOn(convertStringToInstant(delphiResponse.getDatetimeRequest(), DATE_FORMAT))
                .uuidInstance(data.getUuid())
                .spedi03(getData(data.getSpedi03()))
                .spedi04(getData(data.getSpedi04()))
                .spedi05(getData(data.getSpedi05()))
                .spedi08(getData(data.getSpedi08()))
                .build();
    }

    private void addPartnerInfo(PartnerDetailsForCommercialAgreementQueryResponse partnerData) {
        if (customer == null || partnerData == null) {
            return;
        }

        customer.title = partnerData.getPersonTitle();
        customer.middleName = partnerData.getPersonMiddleName();
        customer.surname = partnerData.getPersonSurname();
        customer.forename = partnerData.getPersonForenames();


        var dateOfBirth = convertStringToInstant(partnerData.getDateOfBirth(), DATE_FORMAT);

        if (dateOfBirth != null) {
            customer.dateOfBirth = dateOfBirth;
            customer.age = calculateAge(dateOfBirth.atZone(ZoneId.systemDefault()).toLocalDate());
        }

        addAddressPartnerForCommercialCustomer(partnerData);
    }

    public void addPersonalInfo(AgreementDetail agreementDetails, ETraderType traderType) {
        customer.businessLine = EBusinessLine.PERSONAL;
        this.incorporationStatus = null;
        var dateOfBirth = convertStringToInstant(agreementDetails.getPersonDateOfBirth(), DATE_FORMAT);

        customer.title = agreementDetails.getPersonTitle();
        customer.surname = agreementDetails.getPersonNameLast();
        customer.forename = agreementDetails.getPersonNameFirst();
        customer.middleName = agreementDetails.getPersonNameMiddle();

        if (dateOfBirth != null) {
            customer.dateOfBirth = dateOfBirth;
            customer.age = calculateAge(dateOfBirth.atZone(ZoneId.systemDefault()).toLocalDate());
        }

        customer.traderType = traderType;

        addPersonalAddress(agreementDetails);
    }

    private int calculateAge(LocalDate birthDate) {
        return Optional.ofNullable(birthDate).map(date -> between(birthDate, LocalDate.now()).getYears()).orElse(0);
    }

    private Instant convertStringToInstant(String date, String patternString) {
        return Optional.ofNullable(date).map(stringDate -> LocalDateTime.parse(date, ofPattern(patternString)).atZone(ZoneId.systemDefault()).toInstant()).orElse(null);
    }

    public void addCommercialInfo(AgreementDetail agreementDetails,
                                  PartnerDetailsForCommercialAgreementQueryResponse partnerDetailsForCommercialAgreementQueryResponse,
                                  ETraderType traderType) {
        customer.businessLine = EBusinessLine.COMMERCIAL;
        customer.companyName = agreementDetails.getCompanyName();
        customer.companyNumber = agreementDetails.getCompanyRegistrationNumber();
        customer.traderType = traderType;
        addPartnerInfo(partnerDetailsForCommercialAgreementQueryResponse);
    }

    private void addAddressPartnerForCommercialCustomer(PartnerDetailsForCommercialAgreementQueryResponse partnerData) {
        var address = new Address();
        address.country = partnerData.getCountry();
        address.houseName = partnerData.getHouseName();
        address.houseNumber = partnerData.getHouseNumber();
        address.postcode = partnerData.getPostalCode();
        address.postTown = partnerData.getPostTown();
        address.street = partnerData.getStreetName();
        customer.address = address;

    }

    private void addPersonalAddress(AgreementDetail agreementDetails) {
        var address = new Address();
        address.street = agreementDetails.getPersonStreetName();
        address.country = agreementDetails.getPersonCountry();
        address.postTown = agreementDetails.getPersonPostTown();
        address.postcode = agreementDetails.getPersonPostCode();
        address.houseName = agreementDetails.getPersonHouseName();
        address.houseNumber = agreementDetails.getPersonHouseNumber();

        customer.address = address;
    }

    @JsonIgnore
    public boolean isUnderAge() {
        var isNotNull = customer != null && customer.dateOfBirth != null && customer.age != null;

        return isNotNull && customer.age < 18;
    }


    public void addDecision(EStatus providedStatus) {
        var isStatusOnScheme = providedStatus != null && providedStatus.equals(ON_SCHEME);
        var isOnScheme = isUnderAge() || isStatusOnScheme;

        this.status = isOnScheme ? EStatus.ON_SCHEME : Optional.ofNullable(providedStatus).orElse(EStatus.NOT_ON_SCHEME);


        this.automatedDecisionDate = Instant.now();
        if (isOnScheme) {
            this.automatedDecision = FAIL;
            this.finalDecision = FAIL;
            finalSchemeAssigned = ESchemaAssigned.YES;
            automatedSchemeAssigned = ESchemaAssigned.YES;
        } else {
            this.automatedDecision = PASS;
            this.finalDecision = PASS;
            finalSchemeAssigned = ESchemaAssigned.NO;
            automatedSchemeAssigned = ESchemaAssigned.NO;
        }
    }

    public void addLastOutcome(AffordabilitySummary lastOutcome) {
        this.statusBefore = lastOutcome.status;
    }


    public void markAsError(String reason) {
        this.automatedDecisionDate = Instant.now();
        this.automatedDecision = ERROR;
        this.finalDecision = ERROR;
        finalSchemeAssigned = ESchemaAssigned.ERROR;
        automatedSchemeAssigned = ESchemaAssigned.ERROR;
        this.reason = reason;
    }

    public void addIncorporationStatus(EIncorporationStatus status) {
        this.incorporationStatus = status;
    }

    @JsonIgnore
    public HttpEntity<Map<String, String>> getBodyToCraUpdate() {
        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        Map<String, String> requestMap = new HashMap<>();
        requestMap.put("agreement_number", agreementNumber);
        requestMap.put("environment", environment);
        requestMap.put("status", status.getText());

        return new HttpEntity<>(requestMap, headers);
    }

    @JsonIgnore
    public boolean isAllowedToUpdateCra() {
        var isDecisionAutomatic = automatedDecision != null;
        var shouldSave = isDecisionAutomatic && isFunded && customerSegment != null;
        return shouldSave || isUnderAge();
    }

    public void markAsUnFunded(){
        this.exclusionReason = EExclusionReason.UN_FUNDED;
        this.isFunded = false;
    }
}
