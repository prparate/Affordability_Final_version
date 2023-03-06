package com.affordability.service;

import com.affordability.dto.request.DataFetcherRequest;
import com.affordability.dto.response.DataFetcherResponse;
import com.affordability.service.datafetcher.IAgreementSummaryService;
import com.affordability.service.datafetcher.IPartnerDetailsForCommercialAgreement;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.request.PartnerForCommercialAgreementRequest;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response.PartnerDetailsForCommercialAgreementQueryResponse;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response.PartnerDetailsForCommercialAgreementResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.webjars.NotFoundException;

import java.io.IOException;
import java.io.InputStream;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;

@SpringBootTest
class DataFetcherServiceTest {

    @Autowired
    private IAgreementSummaryService service;

    @Autowired
    private IPartnerDetailsForCommercialAgreement partnerDetailsForCommercialAgreement;

    @MockBean
    private RestTemplate restTemplate;


    private static InputStream readJson(String fileName) throws IOException {
        var inputStream = Thread.currentThread()
                .getContextClassLoader()
                .getResourceAsStream(fileName);

        return inputStream;

    }

    @Test
    @DisplayName("Should return success if parameters are ok and exists values")
    void shouldReturnSuccessWhenParametersAreOk() throws Exception {

        var map = new ObjectMapper();
        var bodyResponse = map.readValue(readJson("mock/success-response-data-fetcher.json"), DataFetcherResponse.class);

        when(restTemplate.postForObject(anyString(), any(DataFetcherRequest.class), any()))
                .thenReturn(bodyResponse);

        var response = service.getAgreementSummary("000010111");


        assertNotNull(response);
    }

    @Test
    @DisplayName("Should throw notFoundException when dont have values")
    void shouldThrowNotFoundExceptionWhenDontHaveValues() throws Exception {

        var map = new ObjectMapper();
        var bodyResponse = map.readValue(readJson("mock/no-values-response-data-fetcher.json"), DataFetcherResponse.class);

        when(restTemplate.postForObject(any(), any(DataFetcherRequest.class), any()))
                .thenReturn(ResponseEntity.ok(bodyResponse));

        assertThrows(NotFoundException.class, () -> service.getAgreementSummary("000010111"));
    }

    @Test
    @DisplayName("Should throw exception when http code is 500")
    void shouldThrowExceptionWithStatus500() throws Exception {

        when(restTemplate.postForObject(anyString(), any(DataFetcherRequest.class), any()))
                .thenThrow(new RestClientException("connection issue"));


        assertThrows(RestClientException.class,
                () -> service.getAgreementSummary("000010111"));
    }

    @Test
    @DisplayName("Should throw exception when environment param is wrong")
    void shouldThrowErrorWhenParamEnvIsWrong() throws Exception {
        assertThrows(Exception.class,
                () -> service.getAgreementSummary("000010111"));
    }

    @Test
    @DisplayName("Should return success when Partner details for Commercial agreement is returned from Data Fetcher")
    void shouldReturnSuccessWhenPartnerDetailsReturnedFromDataFetcher() throws Exception {

        var agreementNumber = "000110407";
        var map = new ObjectMapper();
        var bodyResponse = map.readValue(readJson("mock/data-fetcher-partner-details/success-response-data-fetcher-partner-details.json"), PartnerDetailsForCommercialAgreementResponse.class);

        when(restTemplate.postForObject(anyString(), any(PartnerForCommercialAgreementRequest.class), any()))
                .thenReturn(bodyResponse);

        var response = partnerDetailsForCommercialAgreement.getFirstPartnerDetailsForCommercialAgreement(agreementNumber);

        assertNotNull(response);
    }

    @Test
    @DisplayName("Should throw NotFoundException when Partner Details are not present")
    void shouldThrowNotFoundExceptionWhenPartnerDetailsAreNotPresent() throws Exception {

        var agreementNumber = "000110407";
        var map = new ObjectMapper();
        var bodyResponse = map.readValue(readJson("mock/data-fetcher-partner-details/no-values-response-data-fetcher-partner-details.json"), PartnerDetailsForCommercialAgreementResponse.class);

        when(restTemplate.postForObject(any(), any(PartnerForCommercialAgreementRequest.class), any()))
                .thenReturn(ResponseEntity.ok(bodyResponse));

        assertThrows(NotFoundException.class, () -> partnerDetailsForCommercialAgreement.getFirstPartnerDetailsForCommercialAgreement(agreementNumber));
    }
}
