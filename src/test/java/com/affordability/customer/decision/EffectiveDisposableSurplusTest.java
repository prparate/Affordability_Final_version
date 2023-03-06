package com.affordability.customer.decision;

import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import com.affordability.service.cra.response.DataSegmentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class EffectiveDisposableSurplusTest {

    @Autowired
    private IEffectiveDisposableSurplus effectiveDisposableSurplus;

    @Test
    void getEffectiveDisposableSurplus() {

        var consumerSummary = new ConsumerSummaryDelphiDataResponse();

        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "69.89"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "1"));
        consumerSummary.setSpedi05(new DataSegmentResponse("SPEDI05", "1"));
        consumerSummary.setSpedi06(new DataSegmentResponse("SPEDI06", "-1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "1"));

        assertEquals(BigDecimal.valueOf(67).setScale(2), effectiveDisposableSurplus.getEffectiveDisposableSurplus(consumerSummary),
                "Check value of attributes at index ");

    }
    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMocksForEffectiveDisposableSurplusForGreaterThanZero")
    @DisplayName("EffectiveDisposableSurplus should be greater than zero")
    void getEffectiveDisposableSurplus(List<ConsumerSummaryDelphiDataResponse> consumerSummaryList) {
        var index = 0;
        for (ConsumerSummaryDelphiDataResponse consumerSummary : consumerSummaryList) {
            assertTrue(effectiveDisposableSurplus.getEffectiveDisposableSurplus(consumerSummary).compareTo(BigDecimal.ZERO) > 0, "Check value of attributes at index '" + index + "' of List<DelphiDataResponse>");
        }
    }

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMocksForEffectiveDisposableSurplusForLessThanZero")
    @DisplayName("EffectiveDisposableSurplus should be less than zero")
    void getEffectiveDisposableSurplusForLessThanZero(List<ConsumerSummaryDelphiDataResponse> consumerSummaryList) {
        var index = 0;
        for (ConsumerSummaryDelphiDataResponse consumerSummary : consumerSummaryList) {
            assertTrue(effectiveDisposableSurplus.getEffectiveDisposableSurplus(consumerSummary).compareTo(BigDecimal.ZERO) < 0, "Check value of attributes at index '" + index + "' of List<DelphiDataResponse>");
            index++;
        }
    }

    static Stream<List<ConsumerSummaryDelphiDataResponse>> CreateJsonPropertyMocksForEffectiveDisposableSurplusForGreaterThanZero() {
        var mocks = new ArrayList<ConsumerSummaryDelphiDataResponse>();
        var delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03", "3200"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1500"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "500"));
        delBlock.setSpedi06(new DataSegmentResponse("SPEDI06", "700"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "200"));

        mocks.add(delBlock);


        delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03", "2000"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1001"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "300"));
        delBlock.setSpedi06(new DataSegmentResponse("SPEDI06", "300"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "300"));
        mocks.add(delBlock);


        delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03", "1999"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1000"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "200"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "400"));
        mocks.add(delBlock);

        return Stream.of(mocks);
    }


    static Stream<List<ConsumerSummaryDelphiDataResponse>> CreateJsonPropertyMocksForEffectiveDisposableSurplusForLessThanZero() {
        var mocks = new ArrayList<ConsumerSummaryDelphiDataResponse>();
        var delBlock = new ConsumerSummaryDelphiDataResponse();


        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03", "3200"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1600"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "1600"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "200"));
        mocks.add(delBlock);


        delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03", "2000"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1001"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "1000"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "300"));
        mocks.add(delBlock);


        delBlock = new ConsumerSummaryDelphiDataResponse();

        delBlock.setSpedi03(new DataSegmentResponse("SPEDI03", "1999"));
        delBlock.setSpedi04(new DataSegmentResponse("SPEDI04", "1000"));
        delBlock.setSpedi05(new DataSegmentResponse("SPEDI05", "900"));
        delBlock.setSpedi08(new DataSegmentResponse("SPEDI08", "400"));
        mocks.add(delBlock);

        return Stream.of(mocks);
    }
}