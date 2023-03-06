package com.affordability.service.cra;

import com.affordability.exception.CustomerNotFoundException;
import com.affordability.exception.MicroservicesConnectionException;
import com.affordability.service.cra.request.CraRequest;
import com.affordability.service.cra.response.ConsumerSummaryResponse;
import com.affordability.service.cra.response.DelphiResponse;
import com.affordability.utils.CraExperianUrlBuilder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpMethod;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.lang.reflect.Field;
import java.net.URLDecoder;
import java.nio.charset.StandardCharsets;

@Service
@Slf4j
public class CraService implements ICraService {

    @Value("${cra.experian-base-url}")
    private String craExperianBaseUrl;

    @Autowired
    @Qualifier("CraDelphi")
    private RestTemplate restTemplate;


    private static final String API_VERSION = "v1";
    private static final String OPERATION_CREDIT_CHECK = "credit/check";
    private static final String OPERATION_AFFORDABILITY_CHECK = "affordability/check";

    @Autowired
    private RetryTemplate retryTemplate;

    @Override
    public DelphiResponse getDelphiData(CraRequest agreementDetail) throws Exception {

        CraExperianUrlBuilder craExperianUrlBuilder = new CraExperianUrlBuilder();

        var delphiUrl = craExperianUrlBuilder.buildCraExperianUrl(agreementDetail, craExperianBaseUrl, API_VERSION, OPERATION_CREDIT_CHECK);

        return requestDelphiData(delphiUrl, agreementDetail.getAgreementNumber());
    }

    @Override
    public ConsumerSummaryResponse getConsumerData(CraRequest agreementDetail) throws Exception {

        CraExperianUrlBuilder craUrlBuilder = new CraExperianUrlBuilder();

        var craDelphiDataUrl = craUrlBuilder.buildCraExperianUrl(agreementDetail, craExperianBaseUrl, API_VERSION, OPERATION_AFFORDABILITY_CHECK);

        return requestConsumerSummary(craDelphiDataUrl, agreementDetail.getAgreementNumber());

    }

    private DelphiResponse requestDelphiData(String delphiUrl, String agreementNumber) throws Exception {

        String craDelphiDataUrl = URLDecoder.decode(delphiUrl, StandardCharsets.UTF_8.name());

        return retryTemplate.execute(
                value -> {
                    log.info("CRA Request url for Customer Agreement {} to fetch Delphi block data : {}", agreementNumber, craDelphiDataUrl);

                    var response = restTemplate.getForObject(craDelphiDataUrl, DelphiResponse.class);
                    log.info("Response from CRA for Delphi block data for Customer Agreement {} : {}", agreementNumber, response);

                    throwExceptionForEmptyOrErrorResponse(response, agreementNumber);

                    return response;
                },
                value -> {
                    var errorMessage = value.getLastThrowable().getMessage();
                    log.error("Error from CRA for Delphi block data: {}", errorMessage);
                    throw new MicroservicesConnectionException(HttpMethod.GET, craDelphiDataUrl, errorMessage);
                });
    }

    private ConsumerSummaryResponse requestConsumerSummary(String craUrl, String agreementNumber) throws Exception {

        String craServiceUrl = URLDecoder.decode(craUrl, StandardCharsets.UTF_8.name());
        return retryTemplate.execute(
                value -> {
                    log.info("CRA Request url for Customer Agreement {} to fetch Consumer Summary : {}", agreementNumber, craServiceUrl);

                    var response = restTemplate.getForObject(craServiceUrl, ConsumerSummaryResponse.class);
                    log.info("Response from CRA for Consumer Summary for agreement {} : {}", agreementNumber, response);

                    throwExceptionForEmptyOrErrorResponse(response, agreementNumber);

                    return response;
                },
                value -> {
                    var errorMessage = value.getLastThrowable().getMessage();
                    log.error("Error from CRA for Consumer Summary data for Customer Agreement {} : {}", agreementNumber, errorMessage);
                    throw new MicroservicesConnectionException(HttpMethod.GET, craUrl, errorMessage);
                });
    }


    private void throwExceptionForEmptyOrErrorResponse(DelphiResponse response, String agreementNumber) throws Exception {
        throwExceptionIfNullResponse(response, agreementNumber);
        throwExceptionIfErrorMessageHasBeenReceived(response, agreementNumber);
        throwExceptionIfAllFieldsAreNull(response, agreementNumber);
    }

    private void throwExceptionForEmptyOrErrorResponse(ConsumerSummaryResponse response, String agreementNumber) throws Exception {
        throwExceptionIfNullResponse(response, agreementNumber);
        throwExceptionIfErrorMessageHasBeenReceived(response, agreementNumber);
        throwExceptionIfAllFieldsAreNull(response, agreementNumber);
    }

    private void throwExceptionIfNullResponse(DelphiResponse response, String agreementNumber) throws CustomerNotFoundException {
        if (response == null || response.getData() == null) {
            throw new CustomerNotFoundException("No data received from CRA for Customer Agreement " + agreementNumber);
        }
    }

    private void throwExceptionIfNullResponse(ConsumerSummaryResponse response, String agreementNumber) throws CustomerNotFoundException {
        if (response == null || response.getData() == null) {
            throw new CustomerNotFoundException("No data received from CRA for Customer Agreement " + agreementNumber);
        }
    }

    private void throwExceptionIfErrorMessageHasBeenReceived(DelphiResponse response, String agreementNumber) throws CustomerNotFoundException {
        if (response.getData().getErrorMessage() != null) {
            var errorMessage = response.getData().getErrorMessage().getValue();
            throw new CustomerNotFoundException("Error message for Customer Agreement " + agreementNumber + " : " + errorMessage);
        }
    }

    private void throwExceptionIfErrorMessageHasBeenReceived(ConsumerSummaryResponse response, String agreementNumber) throws CustomerNotFoundException {
        if (response.getData().getErrorMessage() != null) {
            var errorMessage = response.getData().getErrorMessage().getValue();
            throw new CustomerNotFoundException("Error message for Customer Agreement " + agreementNumber + " : " + errorMessage);
        }
    }

    private void throwExceptionIfAllFieldsAreNull(DelphiResponse response, String agreementNumber) throws Exception {
        boolean allFieldsReceivedAreNull = true;

        for (Field f : response.getData().getClass().getDeclaredFields()) {
            f.setAccessible(true);

            if (f.get(response.getData()) != null) {

                allFieldsReceivedAreNull = false;
                break;
            }
        }

        if (allFieldsReceivedAreNull) {
            throw new CustomerNotFoundException("All data fields received from CRA are null for Customer Agreement " + agreementNumber);
        }
    }

    private void throwExceptionIfAllFieldsAreNull(ConsumerSummaryResponse response, String agreementNumber) throws Exception {
        boolean allFieldsReceivedAreNull = true;

        for (Field f : response.getData().getClass().getDeclaredFields()) {
            f.setAccessible(true);

            if (f.get(response.getData()) != null) {

                allFieldsReceivedAreNull = false;
                break;
            }
        }

        if (allFieldsReceivedAreNull) {
            throw new CustomerNotFoundException("All data fields received from CRA are null for Customer Agreement " + agreementNumber);
        }
    }
}
