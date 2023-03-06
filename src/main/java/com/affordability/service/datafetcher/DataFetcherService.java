package com.affordability.service.datafetcher;


import com.affordability.dto.request.DataFetcherArgument;
import com.affordability.dto.request.DataFetcherRequest;
import com.affordability.dto.response.AgreementDetail;
import com.affordability.dto.response.DataFetcherResponse;
import com.affordability.exception.ApplicationException;
import com.affordability.service.IEnvironmentProvider;
import com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.request.DataForDetermineRegulationArgumentRequest;
import com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.request.DataForDetermineRegulationRequest;
import com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.response.DataForDetermineRegulationResponse;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.request.PartnerForCommercialAgreementArgumentRequest;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.request.PartnerForCommercialAgreementRequest;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response.PartnerDetailsForCommercialAgreementQueryResponse;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response.PartnerDetailsForCommercialAgreementResponse;
import lombok.RequiredArgsConstructor;
import lombok.SneakyThrows;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;

import java.math.BigDecimal;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataFetcherService implements IDetermineRegulationService, IAgreementSummaryService, IPartnerDetailsForCommercialAgreement {

    private final IEnvironmentProvider environmentProvider;

    @Value("${data.fetcher.base-url}")
    private String baseUrl;

    @Value("${data.fetcher.number-of-partners:3}")
    private Integer numberOfRetries;

    @Value("${data.fetcher.number-of-partners:3}")
    private Integer intervalInSeconds;
    private final RestTemplate restTemplate;

    @Override
    @SneakyThrows
    public AgreementDetail getAgreementSummary(final String agreementNumber) {
        var queryFileName = "agreement_by_agreement_number_excluding_fee_component";
        var request = buildAgreementsByAgreementNumbersRequest(agreementNumber);
        DataFetcherResponse response = null;
        var retryCount = 0;
        do {
            retryCount++;
            response = this.executeQuery(queryFileName, request, DataFetcherResponse.class);
            if(response != null && response.getData() != null
                && response.getData().get(0).getAgrInstalment().equals(BigDecimal.ZERO)){
                Thread.sleep(intervalInSeconds.longValue() * 1000L);
            }

        } while (response != null &&
            retryCount < numberOfRetries &&
            response.getData().get(0).getAgrInstalment().equals(BigDecimal.ZERO));


        throwExceptionIfEmptyResponse(response, "No agreement");

        return response.getData().get(0);
    }

    @Override
    public DataForDetermineRegulationResponse getDataForDetermineRegulation(String agreementNumber) {
        var queryFileName = "affordability_customer_data_for_regulation";
        var request = buildDataForDetermineRegulationRequest(agreementNumber);
        log.info("request to DataFetcher to get data to determine regulation : {}", request);

        var response = this.executeQuery(queryFileName, request, DataForDetermineRegulationResponse.class);
        log.info("response from DataFetcher to determine regulation : {}", response);
        return response;
    }

    private <TRequest, TResponse> TResponse executeQuery(String queryFileName, TRequest request, Class<TResponse> responseType) {
        String environment = this.environmentProvider.getPremfinaEnvironmentName();
        String url = buildUrl(environment, queryFileName);

        log.info("request url: {}", url);
        log.info("request body: {}", request);

        var response = restTemplate.postForObject(url, request, responseType);

        log.info("response from DataFetcher : {}", response);

        return response;
    }

    private String buildUrl(String envRealName, String queryFileName) {

        return String.format(
                "%s/v1/clients/%s/queries/%s/execute"
                , baseUrl, envRealName, queryFileName
        );
    }

    private DataFetcherRequest buildAgreementsByAgreementNumbersRequest(String agreementNumber) {
        return DataFetcherRequest.builder()
                .arguments(new DataFetcherArgument(agreementNumber))
                .build();
    }

    private DataForDetermineRegulationRequest buildDataForDetermineRegulationRequest(String agreementNumber) {
        var request = new DataForDetermineRegulationRequest();

        request.arguments = new DataForDetermineRegulationArgumentRequest();
        request.arguments.agreementNumber = agreementNumber;

        return request;
    }

    private void throwExceptionIfEmptyResponse(DataFetcherResponse response, String errorMessage) throws Exception {
        if (isInvalidResponseBody(response)) {
            log.error("Error occured while accessing data fetcher: {}", errorMessage);
            throw new NotFoundException(errorMessage);
        }

        var isZeroValues =  response.getData().get(0).getAgrInstalment().equals(BigDecimal.ZERO);
        if (isZeroValues) {
            var agreementDetail = response.getData().get(0);
            throw new ApplicationException(agreementDetail.getAgrAgreementNumber(),"Instalment value returned as zero");
        }
    }

    private boolean isInvalidResponseBody(DataFetcherResponse response) {
        return response == null || response.getData() == null || response.getData().isEmpty();
    }

    @Override
    public PartnerDetailsForCommercialAgreementQueryResponse getFirstPartnerDetailsForCommercialAgreement(String agreementNumber) {

        var queryFileName = "first_partner_details_for_commercial_agreement";
        var request = buildRequestToFetchDetailsOfFirstPartnerForCommercialAgreement(agreementNumber);
        var response = this.executeQuery(queryFileName, request, PartnerDetailsForCommercialAgreementResponse.class);
        log.info("response from DataFetcher regarding Partner details for Commercial Agreement : {}", response);

        throwExceptionIfEmptyPartnerDetails(response, "No Partner details found for Commercial agreement");

        return response.getData().get(0);

    }

    private PartnerForCommercialAgreementRequest buildRequestToFetchDetailsOfFirstPartnerForCommercialAgreement(String agreementNumber) {
        return PartnerForCommercialAgreementRequest.builder()
                .arguments(new PartnerForCommercialAgreementArgumentRequest(agreementNumber))
                .build();
    }

    private void throwExceptionIfEmptyPartnerDetails(PartnerDetailsForCommercialAgreementResponse response, String errorMessage) {
        if (isInvalidPartnerDetails(response)) {
            log.error("Error occured while accessing Partner details for Commercial Agreement from data fetcher: {}", errorMessage);
            throw new NotFoundException(errorMessage);
        }
    }

    private boolean isInvalidPartnerDetails(PartnerDetailsForCommercialAgreementResponse response) {
        return response == null || response.getData() == null || response.getData().isEmpty();
    }
}
