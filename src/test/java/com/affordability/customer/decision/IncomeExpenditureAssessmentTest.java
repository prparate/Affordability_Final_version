package com.affordability.customer.decision;


import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import com.affordability.model.EStatus;
import com.affordability.service.cra.response.DataSegmentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class IncomeExpenditureAssessmentTest {

    private IIncomeExpenditureAssessment incomeExpenditureAssessment = new IncomeExpenditureAssessment();

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForNotOnScheme")
    @DisplayName("IncomeExpenditureAssessmentTest should return Not On Scheme")
    void checkOnSchemeReturnsNotONScheme(List<ConsumerSummaryDelphiDataResponse> consumerSummaryList) {
        var index = 0;
        for (ConsumerSummaryDelphiDataResponse consumerSummary : consumerSummaryList) {
            var expenditure = incomeExpenditureAssessment.calculate(consumerSummary,new BigDecimal(100));
            assertEquals(EStatus.NOT_ON_SCHEME, incomeExpenditureAssessment.checkOnScheme(expenditure), "Check value of attributes at index '" + index + "' of List<ConsumerSummary>");
        }
    }


    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForOnScheme")
    @DisplayName("IncomeExpenditureAssessmentTest should return On Scheme")
    void checkOnSchemeReturnsOnScheme(List<ConsumerSummaryDelphiDataResponse> consumerSummaryList) {
        var index = 0;
        for (ConsumerSummaryDelphiDataResponse consumerSummary : consumerSummaryList) {
            var expenditure = incomeExpenditureAssessment.calculate(consumerSummary,new BigDecimal(100));
            assertEquals(EStatus.ON_SCHEME, incomeExpenditureAssessment.checkOnScheme(expenditure), "Check value of attributes at index '" + index + "' of List<ConsumerSummary>");
        }
    }

    static Stream<List<ConsumerSummaryDelphiDataResponse>> CreateJsonPropertyMockForNotOnScheme() {
        var mocks = new ArrayList<ConsumerSummaryDelphiDataResponse>();

        var delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03",  "3200"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1500"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "500"));
        delBlock.setSpedi06(new DataSegmentResponse("SPEDI06", "700"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "200"));

        mocks.add(delBlock);


        delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03",  "2000"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1001"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "300"));
        delBlock.setSpedi06(new DataSegmentResponse("SPEDI06", "300"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "300"));
        mocks.add(delBlock);


        delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03",  "1999"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1000"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "200"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "400"));
        mocks.add(delBlock);

        return Stream.of(mocks);
    }


    static Stream<List<ConsumerSummaryDelphiDataResponse>> CreateJsonPropertyMockForOnScheme() {
        var mocks = new ArrayList<ConsumerSummaryDelphiDataResponse>();

        var delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03",  "1000"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "900"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "300"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "200"));

        mocks.add(delBlock);

        delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03",  "2001"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1600"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "200"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "300"));
        mocks.add(delBlock);

        delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03",  "1990"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "100"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "800"));
        delBlock.setSpedi06(new DataSegmentResponse("SPEDI06", "600"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "1200"));
        mocks.add(delBlock);

        delBlock = new ConsumerSummaryDelphiDataResponse();
        mocks.add(delBlock);

        return Stream.of(mocks);
    }

    @Test
    void getEffectiveDisposableSurplus() {

        var consumerSummary = new ConsumerSummaryDelphiDataResponse();

        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "69.89"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "1"));
        consumerSummary.setSpedi05(new DataSegmentResponse("SPEDI05", "1"));
        consumerSummary.setSpedi06(new DataSegmentResponse("SPEDI06", "-1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "1"));

        assertEquals(BigDecimal.valueOf(12).setScale(2), incomeExpenditureAssessment.calculate(consumerSummary,new BigDecimal("55")), "Wrong Calculation");

    }
    @Test
    void getEffectiveDisposableSurplusForUnder25K() {

        var consumerSummary = new ConsumerSummaryDelphiDataResponse();

        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "2000"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "600"));
        consumerSummary.setSpedi05(new DataSegmentResponse("SPEDI05", "300"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "400"));

        assertEquals(BigDecimal.valueOf(623.00).setScale(2),incomeExpenditureAssessment.calculate(consumerSummary,new BigDecimal("110")), "Wrong Calculation");

    }
}