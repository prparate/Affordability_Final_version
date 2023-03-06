package com.affordability.model;

import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import com.affordability.service.cra.response.DataSegmentResponse;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

class ConsumerSummaryDelphiDataTest {

    @ParameterizedTest
    @MethodSource("createMockConsumerSummaryList")
    void hasNullOrNegative(List<ConsumerSummaryDelphiDataResponse> consumerSummaryDelphiDataList) {
        for (ConsumerSummaryDelphiDataResponse ConsumerSummaryDelphiDataResponse : consumerSummaryDelphiDataList) {
            System.out.println(ConsumerSummaryDelphiDataResponse);
            assertTrue(ConsumerSummaryDelphiDataResponse.hasNullNegativeOrInvalidValue(), "Should return ture if has null or -ve value");
        }
    }


    @ParameterizedTest
    @MethodSource("createValidMockConsumerSummaryList")
    void hasNullOrNegativeRetursFalse(List<ConsumerSummaryDelphiDataResponse> consumerSummaryDelphiDataList) {
        for (ConsumerSummaryDelphiDataResponse ConsumerSummaryDelphiDataResponse : consumerSummaryDelphiDataList) {
            System.out.println(ConsumerSummaryDelphiDataResponse);
            assertFalse(ConsumerSummaryDelphiDataResponse.hasNullNegativeOrInvalidValue(), "Should return ture if has null or -ve value");
        }
    }


    static Stream<List<ConsumerSummaryDelphiDataResponse>> createMockConsumerSummaryList() {

        var mockList = new ArrayList<ConsumerSummaryDelphiDataResponse>();
        var nullConsumerSummary = new ConsumerSummaryDelphiDataResponse();
        mockList.add(nullConsumerSummary);


        var allNegConsumerSummary = new ConsumerSummaryDelphiDataResponse();
        allNegConsumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "-1"));
        allNegConsumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "-1"));
        allNegConsumerSummary.setSpedi05(new DataSegmentResponse("SPEDI05", "-1"));
        allNegConsumerSummary.setSpedi06(new DataSegmentResponse("SPEDI06", "-1"));
        allNegConsumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "-1"));
        mockList.add(allNegConsumerSummary);


        allNegConsumerSummary = new ConsumerSummaryDelphiDataResponse();
        allNegConsumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "-2"));
        allNegConsumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "-2"));
        allNegConsumerSummary.setSpedi05(new DataSegmentResponse("SPEDI05", "-2"));
        allNegConsumerSummary.setSpedi06(new DataSegmentResponse("SPEDI06", "-2"));
        allNegConsumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "-2"));
        mockList.add(allNegConsumerSummary);


        var consumerSummary = new ConsumerSummaryDelphiDataResponse();
        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "1"));
        mockList.add(consumerSummary);

        consumerSummary = new ConsumerSummaryDelphiDataResponse();
        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "1"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "1"));

        mockList.add(consumerSummary);

        consumerSummary = new ConsumerSummaryDelphiDataResponse();
        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "1"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "1"));
        consumerSummary.setSpedi05(new DataSegmentResponse("SPEDI05", "1"));

        mockList.add(consumerSummary);

        consumerSummary = new ConsumerSummaryDelphiDataResponse();
        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "1"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI05", "1"));

        mockList.add(consumerSummary);

        consumerSummary = new ConsumerSummaryDelphiDataResponse();
        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "1"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI05", "1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "-1"));


        consumerSummary = new ConsumerSummaryDelphiDataResponse();
        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "1"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI05", "1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "-2"));
        mockList.add(consumerSummary);

        return Stream.of(mockList);
    }


    static Stream<List<ConsumerSummaryDelphiDataResponse>> createValidMockConsumerSummaryList() {

        var mockList = new ArrayList<ConsumerSummaryDelphiDataResponse>();
        var consumerSummary = new ConsumerSummaryDelphiDataResponse();
        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "1"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "1"));
        consumerSummary.setSpedi05(new DataSegmentResponse("SPEDI05", "1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "2"));
        mockList.add(consumerSummary);


        consumerSummary = new ConsumerSummaryDelphiDataResponse();
        consumerSummary.setSpedi03(new DataSegmentResponse("SPEDI03", "3"));
        consumerSummary.setSpedi04(new DataSegmentResponse("SPEDI04", "3"));
        consumerSummary.setSpedi05(new DataSegmentResponse("SPEDI05", "1"));
        consumerSummary.setSpedi08(new DataSegmentResponse("SPEDI08", "3"));
        mockList.add(consumerSummary);

        return Stream.of(mockList);

    }
}