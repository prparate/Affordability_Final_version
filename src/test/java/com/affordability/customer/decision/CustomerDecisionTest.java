package com.affordability.customer.decision;

import com.affordability.customer.segmentation.ECustomerSegment;
import com.affordability.dto.response.AffordabilityDelphiDataResponse;
import com.affordability.dto.response.AgreementDetail;
import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import com.affordability.model.EDecisionType;
import com.affordability.model.ELowInstalment;
import com.affordability.model.EStatus;
import com.affordability.service.CraServiceMock;
import com.affordability.service.cra.request.CraRequest;
import com.affordability.service.cra.response.ConsumerSummaryResponse;
import com.affordability.service.cra.response.DataSegmentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class CustomerDecisionTest {

    @Autowired
    IncomeExpenditureAssessment incomeExpenditureAssessment;

    @Autowired
    EffectiveDisposableSurplus effectiveDisposableSurplus;

    @Autowired
    CustomerDecision customerDecision;

    CraServiceMock craServiceMock = new CraServiceMock();

    @ParameterizedTest
    @MethodSource("createMockAgreementDetailResponse")
    @DisplayName("should return value 'No Schema' when Prime customer instalment less than equal 56")
    void shouldReturnNoSchemeWhenPrimeCustomerInstalmentLessThanEqual56(AgreementDetail agreementDetail) throws Exception {
        var primeSegment = ECustomerSegment.PRIME;
        customerDecision = CreateCustomerDecision();
        var decisionOutcome = customerDecision.processCustomerSegment(primeSegment, new AffordabilityDelphiDataResponse(), new CraRequest(), agreementDetail);

        assertEquals(EStatus.NOT_ON_SCHEME, decisionOutcome.status);
        assertEquals(ELowInstalment.YES, decisionOutcome.lowInstalment);
    }

    @Test
    @DisplayName("should return Not Checked value when Prime customer instalment is null")
    void shouldReturnNotCheckedWhenPrimeCustomerInstalmentIsNull() throws Exception {
        var primeSegment = ECustomerSegment.PRIME;

        customerDecision = CreateCustomerDecision();

        assertEquals(EStatus.NOT_CHECKED,
            (customerDecision.processCustomerSegment(
                primeSegment,
                new AffordabilityDelphiDataResponse(),
                new CraRequest(),
                new AgreementDetail())).status);
    }

    @Test
    @DisplayName("should return value 'No Schema' when Non Prime customer has all null values")
    void shouldReturnOnSchemeWhenNonPrimeCustomerForAllNullValues() throws Exception {
        var nonPrimeSegment = ECustomerSegment.NON_PRIME;
        var response = new ConsumerSummaryResponse();
        response.setData(new ConsumerSummaryDelphiDataResponse());
        craServiceMock.getConsumerDataReturn = response;
        customerDecision = CreateCustomerDecision();
        var decisionOutcome = customerDecision.processCustomerSegment(nonPrimeSegment, new AffordabilityDelphiDataResponse(), new CraRequest(), new AgreementDetail());

        assertEquals(EStatus.ON_SCHEME, decisionOutcome.status);
    }

    static Stream<AgreementDetail> createMockAgreementDetailResponse() {
        AgreementDetail agreementDetail = new AgreementDetail();

        agreementDetail.setAgrInstalment(new BigDecimal(55));

        return Stream.of(agreementDetail);
    }

    @Test
    @DisplayName("Calculate Values")
    void calculateValues() throws Exception {
        var primeSegment = ECustomerSegment.PRIME;
        var agreementDetail = new AgreementDetail();
        agreementDetail.setAgrInstalment(BigDecimal.valueOf(57));
        var consumerSummary = new ConsumerSummaryDelphiDataResponse();

        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "59.09"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "1"));
        consumerSummary.setSpedi05(new DataSegmentResponse("SPEDI05", "1"));
        consumerSummary.setSpedi06(new DataSegmentResponse("SPEDI06", "-1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "1"));

        AffordabilityDelphiDataResponse affordabilityDelphiData = new AffordabilityDelphiDataResponse();

        affordabilityDelphiData.setNdspcii(new DataSegmentResponse("NDSPCII", "39"));
        affordabilityDelphiData.setE1b07(new DataSegmentResponse("E1B07", "U"));
        affordabilityDelphiData.setE1b08(new DataSegmentResponse("E1B08", "N"));
        affordabilityDelphiData.setEa1c01(new DataSegmentResponse("EA1C01", "N"));
        affordabilityDelphiData.setEa4q05(new DataSegmentResponse("EA4Q05", "N"));
        affordabilityDelphiData.setE1a07(new DataSegmentResponse("E1A07", "1"));
        affordabilityDelphiData.setE1d02(new DataSegmentResponse("E1d02", "1"));
        affordabilityDelphiData.setE1b09(new DataSegmentResponse("E1B09", "1"));
        affordabilityDelphiData.setNdecc03(new DataSegmentResponse("NDECC03", "1"));

        customerDecision = CreateCustomerDecision();
        var response = new ConsumerSummaryResponse();
        response.setData(new ConsumerSummaryDelphiDataResponse());

        craServiceMock.getConsumerDataReturn.setData(consumerSummary);
        var decisionOutcome = customerDecision.processCustomerSegment(primeSegment, affordabilityDelphiData, new CraRequest(), agreementDetail);

        assertEquals(EStatus.ON_SCHEME, decisionOutcome.status);
        assertEquals("56.20", decisionOutcome.effectiveDisposableSurplus);
        assertEquals("-0.80", decisionOutcome.incomeExpenditure);
    }

    private CustomerDecision CreateCustomerDecision() {
        var decision = new CustomerDecision(incomeExpenditureAssessment, effectiveDisposableSurplus, craServiceMock);
        return decision;
    }

}
