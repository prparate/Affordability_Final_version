package com.affordability.controller;

import com.affordability.dto.request.AffordabilityVerification;
import com.affordability.dto.request.DataFetcherRequest;
import com.affordability.dto.response.AgreementLastRecordResponse;
import com.affordability.dto.response.DataFetcherResponse;
import com.affordability.model.AffordabilitySummary;
import com.affordability.model.EStatus;
import com.affordability.persistence.IAffordabilityOutcomeRepository;
import com.affordability.persistence.jpa.AffordabilityOutcomeJPA;
import com.affordability.service.cra.response.ConsumerSummaryResponse;
import com.affordability.service.cra.response.DataSegmentResponse;
import com.affordability.service.cra.response.DelphiResponse;
import com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.request.DataForDetermineRegulationRequest;
import com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.response.DataForDetermineRegulationResponse;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response.PartnerDetailsForCommercialAgreementResponse;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.junit.jupiter.api.TestInstance.Lifecycle;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultHandlers;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.context.WebApplicationContext;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.when;
import static org.springframework.http.MediaType.APPLICATION_JSON_VALUE;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@TestInstance(Lifecycle.PER_CLASS)
class AffordabilityControllerTest {
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext webApplicationContext;

    private final ObjectMapper mapper = new ObjectMapper();

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private IAffordabilityOutcomeRepository affordabilityOutcomeRepository;

    @BeforeAll
    public void setup() {
        this.mockMvc = MockMvcBuilders.webAppContextSetup(this.webApplicationContext).build();
    }

    private static InputStream readJson(String fileName) throws IOException {

        return Thread.currentThread()
            .getContextClassLoader()
            .getResourceAsStream(fileName);
    }


    @ParameterizedTest()
    @DisplayName("Execute all AC e2e mocked Data")
    @MethodSource("mockData")
    void executeParameterizedTest(TestControllerParam param) throws Exception {


        when(restTemplate.postForObject(anyString(), any(DataFetcherRequest.class), eq(DataFetcherResponse.class)))
            .thenReturn(param.dataFetcherResponse());

        when(restTemplate.postForObject(anyString(), any(DataForDetermineRegulationRequest.class), eq(DataForDetermineRegulationResponse.class)))
            .thenReturn(param.dataForDetermineRegulationResponse());

        when(restTemplate.postForObject(anyString(), any(), eq(PartnerDetailsForCommercialAgreementResponse.class)))
            .thenReturn(param.partnerDetailsForCommercialAgreementResponse());

        var entity = new AffordabilityOutcomeJPA();
        entity.setId(UUID.randomUUID());

        when(affordabilityOutcomeRepository.save(any())).thenReturn(entity);


        if (param.delphiResponseEx() == null) {
            when(restTemplate.getForObject(anyString(), eq(DelphiResponse.class)))
                .thenReturn(param.delphiResponse());
        } else {
            when(restTemplate.getForObject(anyString(), eq(DelphiResponse.class)))
                .thenThrow(param.delphiResponseEx());
        }

        if (param.consumerSummaryResponseEx() == null) {
            when(restTemplate.getForObject(anyString(), eq(ConsumerSummaryResponse.class)))
                .thenReturn(param.consumerSummaryResponse());
        } else {
            when(restTemplate.getForObject(anyString(), eq(ConsumerSummaryResponse.class)))
                .thenThrow(param.consumerSummaryResponseEx());
        }

        if (param.updateCraResponseException() != null) {
            when(restTemplate.postForObject(anyString(), any(HttpEntity.class), eq(String.class)))
                .thenThrow(param.updateCraResponseException());
        }


        var resultActions = mockMvc.perform(
            MockMvcRequestBuilders.post("/api/verify")
                .accept(APPLICATION_JSON_VALUE)
                .contentType(APPLICATION_JSON_VALUE)
                .content(mapper.writeValueAsString(param.reqBody()))
        );

        resultActions.andDo(MockMvcResultHandlers.print());

        for (final var resultMatcher : param.resultsMatcher()) {
            resultActions.andExpect(resultMatcher);
        }
    }


    private Stream<TestControllerParam> mockData() throws IOException {
        return Stream.of(
            getCommercialPartnershipData(),
            getCommercialNotFundedData(),
            getCommercialNotRegulatedCommercial(),
            getCommercialNotNewData(),

            getPersonalData(),
            getPersonalNotClassifiedData(),
            getPersonalNotClassifiedDataCaseCreditDataIsNotLowerThanZero(),

            getErrorWhenThrowException(),
            getErrorWhenSendingInvalidTransactionType(),

            getErrorCraUpdateShouldNotChangeHttpCodeResponse(),
            getLowInstallmentLowerOrEqualThan56()
        );
    }

    private TestControllerParam getCommercialPartnershipData() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/commercial/agreement-detail.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/commercial/determination-partnership-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);


        var partnerInputStream = readJson("mock/controller/commercial/partner-data.json");
        var mockPartner = map.readValue(partnerInputStream, PartnerDetailsForCommercialAgreementResponse.class);


        var delphiDataInputStream = readJson("mock/controller/commercial/delphi-data.json");
        var mockDelphi = map.readValue(delphiDataInputStream, DelphiResponse.class);

        var consumerDataInputStream = readJson("mock/controller/commercial/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000039574", "new_business", "TPFUAT2");

        var resultMatchers = List.of(
            status().isOk(),
            jsonPath("$.agreement_number").value("000039574"),
            jsonPath("$.environment").value("TPFUAT2"),
            jsonPath("$.activity_type").value("NEW"),
            jsonPath("$.is_funded").value(true),
            jsonPath("$.inclusion_outcome").value("INCLUSION")
        );

        return new TestControllerParam("Commercial data process should return HTTP status code 200", mockAgreementDetail, mockDetermination, mockPartner, mockDelphi, mockConsumer,
            requestBody, resultMatchers, null, null, null);
    }

    private TestControllerParam getCommercialNotNewData() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/commercial/agreement-detail.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/commercial/determination-partnership-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);


        var partnerInputStream = readJson("mock/controller/commercial/partner-data.json");
        var mockPartner = map.readValue(partnerInputStream, PartnerDetailsForCommercialAgreementResponse.class);


        var delphiDataInputStream = readJson("mock/controller/commercial/delphi-data.json");
        var mockDelphi = map.readValue(delphiDataInputStream, DelphiResponse.class);

        var consumerDataInputStream = readJson("mock/controller/commercial/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000039574", "MTA", "TPFUAT2");

        var resultMatchers = List.of(
            status().isOk(),
            jsonPath("$.agreement_number").value("000039574"),
            jsonPath("$.environment").value("TPFUAT2"),
            jsonPath("$.activity_type").value("MTA"),
            jsonPath("$.is_funded").value(true),
            jsonPath("$.inclusion_outcome").value("INCLUSION"),
            jsonPath("$.customer.is_new").value(false)
        );

        return new TestControllerParam("Commercial data should return field is_new as false", mockAgreementDetail, mockDetermination, mockPartner, mockDelphi, mockConsumer,
            requestBody, resultMatchers, null, null, null);
    }


    private TestControllerParam getCommercialNotFundedData() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/commercial/agreement-detail.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/commercial/determination-partnership-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);


        var partnerInputStream = readJson("mock/controller/commercial/partner-data.json");
        var mockPartner = map.readValue(partnerInputStream, PartnerDetailsForCommercialAgreementResponse.class);

        var delphiDataInputStream = readJson("mock/controller/commercial/delphi-data.json");
        var mockDelphi = map.readValue(delphiDataInputStream, DelphiResponse.class);


        var consumerDataInputStream = readJson("mock/controller/commercial/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000039574", "new_business", "TPFUAT");

        var resultMatchers = List.of(
            status().isOk(),
            jsonPath("$.agreement_number").value("000039574"),
            jsonPath("$.environment").value("TPFUAT"),
            jsonPath("$.activity_type").value("NEW"),
            jsonPath("$.is_funded").value(false),
            jsonPath("$.exclusion_reason").value("UN_FUNDED")
        );

        return new TestControllerParam("Commercial not funded data process should return HTTP status code 200", mockAgreementDetail, mockDetermination, mockPartner, mockDelphi, mockConsumer,
            requestBody, resultMatchers, null, null, null);
    }

    private TestControllerParam getCommercialNotRegulatedCommercial() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/commercial/agreement-detail.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/commercial/determination-limited-company-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);

        var partnerInputStream = readJson("mock/controller/commercial/partner-data.json");
        var mockPartner = map.readValue(partnerInputStream, PartnerDetailsForCommercialAgreementResponse.class);

        var delphiDataInputStream = readJson("mock/controller/commercial/delphi-data.json");
        var mockDelphi = map.readValue(delphiDataInputStream, DelphiResponse.class);

        var consumerDataInputStream = readJson("mock/controller/commercial/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000039574", "new_business", "TPFUAT2");

        var resultMatchers = List.of(
            status().isOk(),
            jsonPath("$.agreement_number").value("000039574"),
            jsonPath("$.environment").value("TPFUAT2"),
            jsonPath("$.activity_type").value("NEW"),
            jsonPath("$.is_funded").value(true),
            jsonPath("$.exclusion_reason").value("UN_REGULATED")
        );

        return new TestControllerParam("Commercial not regulated data process should return HTTP status code 200", mockAgreementDetail, mockDetermination, mockPartner, mockDelphi, mockConsumer,
            requestBody, resultMatchers, null, null, null);
    }

    private TestControllerParam getPersonalData() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/personal/agreement-detail.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/personal/determination-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);

        var delphiDataInputStream = readJson("mock/controller/personal/delphi-data.json");
        var mockDelphi = map.readValue(delphiDataInputStream, DelphiResponse.class);

        var consumerDataInputStream = readJson("mock/controller/personal/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000110304", "new_business", "TPFUAT2");

        var resultMatchers = List.of(
            status().isOk(),
            jsonPath("$.agreement_number").value("000110304"),
            jsonPath("$.environment").value("TPFUAT2"),
            jsonPath("$.activity_type").value("NEW"),
            jsonPath("$.is_funded").value(true),
            jsonPath("$.is_regulated").value(true),
            jsonPath("$.status").value("NOT_ON_SCHEME"),
            jsonPath("$.customer.business_line").value("PERSONAL"),
            jsonPath("$.customer.incorporation_status").doesNotExist()
        );

        return new TestControllerParam("Personal data process should return HTTP status code 200", mockAgreementDetail, mockDetermination, null, mockDelphi, mockConsumer,
            requestBody, resultMatchers, null, null, null);
    }

    private TestControllerParam getPersonalNotClassifiedData() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/personal/agreement-detail.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/personal/determination-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);

        var delphiDataInputStream = readJson("mock/controller/personal/delphi-not-classified-data.json");
        var mockDelphi = map.readValue(delphiDataInputStream, DelphiResponse.class);

        var consumerDataInputStream = readJson("mock/controller/personal/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000110304", "new_business", "TPFUAT2");

        var resultMatchers = List.of(
            status().isOk(),
            jsonPath("$.agreement_number").value("000110304"),
            jsonPath("$.environment").value("TPFUAT2"),
            jsonPath("$.activity_type").value("NEW"),
            jsonPath("$.is_funded").value(true),
            jsonPath("$.customer.business_line").value("PERSONAL"),
            jsonPath("$.customer_segment").value("NOT_CLASSIFIED")
        );

        return new TestControllerParam("Personal not classified data process should return HTTP status code 200", mockAgreementDetail, mockDetermination, null, mockDelphi, mockConsumer,
            requestBody, resultMatchers, null, null, null);
    }

    private TestControllerParam getPersonalNotClassifiedDataCaseCreditDataIsNotLowerThanZero() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/personal/agreement-detail.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/personal/determination-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);

        var delphiDataInputStream = readJson("mock/controller/personal/delphi-not-classified-data.json");
        var mockDelphi = map.readValue(delphiDataInputStream, DelphiResponse.class);

        mockDelphi.getData().setE1b09(new DataSegmentResponse("", "1"));
        mockDelphi.getData().setNdecc03(new DataSegmentResponse("", "1"));

        var consumerDataInputStream = readJson("mock/controller/personal/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000110304", "new_business", "TPFUAT2");

        var resultMatchers = List.of(
            status().isOk(),
            jsonPath("$.agreement_number").value("000110304"),
            jsonPath("$.environment").value("TPFUAT2"),
            jsonPath("$.activity_type").value("NEW"),
            jsonPath("$.is_funded").value(true),
            jsonPath("$.customer.business_line").value("PERSONAL"),
            jsonPath("$.customer_segment").value("NOT_CLASSIFIED")
        );

        return new TestControllerParam("Personal not classified with valid credit data should return value from response ", mockAgreementDetail, mockDetermination, null, mockDelphi, mockConsumer,
            requestBody, resultMatchers, null, null, null);
    }

    private TestControllerParam getErrorWhenThrowException() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/personal/agreement-detail.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/personal/determination-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);

        var consumerDataInputStream = readJson("mock/controller/personal/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000110304", "new_business", "TPFUAT2");

        var resultMatchers = List.of(
            status().isBadRequest(),
            jsonPath("$[0].type").value("ERROR"),
            jsonPath("$[0].description").isNotEmpty()

        );

        return new TestControllerParam("Should get error when throw error from request to external services ", mockAgreementDetail, mockDetermination, null, null, mockConsumer,
            requestBody, resultMatchers, new RestClientException("Error to get information"), null, null);
    }

    private TestControllerParam getErrorWhenSendingInvalidTransactionType() throws IOException {
        var requestBody = new AffordabilityVerification("000110304", "test", "TPFUAT2");
        var resultMatchers = List.of(
            status().isBadRequest(),
            jsonPath("$[0].type").value("WARNING"),
            jsonPath("$[0].description").value("Invalid transaction_activity")
        );

        return new TestControllerParam("Should get error when send invalid transaction_activity", null, null, null, null, null, requestBody, resultMatchers, null, null, null);
    }

    private TestControllerParam getErrorCraUpdateShouldNotChangeHttpCodeResponse() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/personal/agreement-detail.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/personal/determination-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);

        var delphiDataInputStream = readJson("mock/controller/personal/delphi-data.json");
        var mockDelphi = map.readValue(delphiDataInputStream, DelphiResponse.class);

        var consumerDataInputStream = readJson("mock/controller/personal/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000110304", "new_business", "TPFUAT2");

        var resultMatchers = List.of(
            status().isOk()
        );

        return new TestControllerParam("Get error when try to update CRA should not change HTTP code 200", mockAgreementDetail, mockDetermination, null, mockDelphi, mockConsumer,
            requestBody, resultMatchers, null, null, new RestClientException("Error to get information"));
    }

    private TestControllerParam getLowInstallmentLowerOrEqualThan56() throws IOException {
        var map = new ObjectMapper();

        var agreementDetailInputStream = readJson("mock/controller/personal/agreement-detail-prime-low-installment.json");
        var mockAgreementDetail = map.readValue(agreementDetailInputStream, DataFetcherResponse.class);

        var determinationInputStream = readJson("mock/controller/personal/determination-data.json");
        var mockDetermination = map.readValue(determinationInputStream, DataForDetermineRegulationResponse.class);

        var delphiDataInputStream = readJson("mock/controller/personal/delphi-data.json");
        var mockDelphi = map.readValue(delphiDataInputStream, DelphiResponse.class);

        var consumerDataInputStream = readJson("mock/controller/personal/consumer-data.json");
        var mockConsumer = map.readValue(consumerDataInputStream, ConsumerSummaryResponse.class);

        var requestBody = new AffordabilityVerification("000110304", "new_business", "TPFUAT2");

        var resultMatchers = List.of(
            status().isOk(),
            jsonPath("$.agreement_number").value("000110304"),
            jsonPath("$.environment").value("TPFUAT2"),
            jsonPath("$.activity_type").value("NEW"),
            jsonPath("$.is_funded").value(true),
            jsonPath("$.status").value("NOT_ON_SCHEME"),
            jsonPath("$.customer.business_line").value("PERSONAL"),
            jsonPath("$.low_instalment").value("YES")
        );

        return new TestControllerParam("Personal data process should return HTTP status code 200", mockAgreementDetail, mockDetermination, null, mockDelphi, mockConsumer,
            requestBody, resultMatchers, null, null, null);
    }

    @Test
    @DisplayName("Get last outcome with valid data should return http code 200")
    void getLastOutcomeWithValidDataShouldReturn200() throws Exception {
        var entity = new AgreementLastRecordResponse("000001", "test", "ON_SCHEME");


        when(affordabilityOutcomeRepository.findLastOutcome(anyString(), anyString()))
            .thenReturn(Optional.of(entity));

        var resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/verify/lastoutcome?agreementNumber=000001&environment=test")
                .accept(APPLICATION_JSON_VALUE)
        );

        resultActions.andExpect(status().isOk())
            .andExpect(jsonPath("$.agreement_number").value("000001"))
            .andExpect(jsonPath("$.status").isNotEmpty())
            .andExpect(jsonPath("$.environment").value("test"));

    }


    @Test
    @DisplayName("When not found data should return http status 404")
    void whenNotFoundDataShouldReturn404() throws Exception {

        when(affordabilityOutcomeRepository.findLastOutcome(anyString(), anyString()))
            .thenReturn(Optional.empty());

        var resultActions = mockMvc.perform(
            MockMvcRequestBuilders.get("/api/verify/lastoutcome?agreementNumber=000001&environment=test")
                .accept(APPLICATION_JSON_VALUE)
        );

        resultActions.andExpect(status().isNotFound());

    }


}
