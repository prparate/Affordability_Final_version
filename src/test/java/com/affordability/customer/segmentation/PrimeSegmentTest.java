package com.affordability.customer.segmentation;

import com.affordability.customer.segmentation.segments.PrimeSegment;
import com.affordability.dto.response.AffordabilityDelphiDataResponse;
import com.affordability.service.cra.response.DataSegmentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PrimeSegmentTest {

    @ParameterizedTest
    @MethodSource("CreateAllCustomerInfoMocksWhichReturnInAnAssignableEvaluation")
    @DisplayName("Should be assignable in every Customer Info scenario")
    void shouldBeAssignableInEveryCustomerInfoScenario(List<ICustomerInfo> allAssignableCustomerInfos) {
        var segment = new PrimeSegment();
        var index = 0;

        for (ICustomerInfo customer: allAssignableCustomerInfos) {
            var isAssignable = segment.isCustomerAssignableToSegment(customer);

            assertTrue(isAssignable, "Expected CustomerInfo in index '" + index + "' be assignable, check how the mock has been set.");

            index++;
        }
    }

    static Stream<List<ICustomerInfo>> CreateAllCustomerInfoMocksWhichReturnInAnAssignableEvaluation(){
        var mocks = new ArrayList<ICustomerInfo>();

        CustomerInfoMock mock;

        mock = new CustomerInfoMock();

        mock.enhancedCustomerIndebtednessIndexForPrime= true;
        mock.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime= true;
        mock.detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime= true;
        mock.settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin = true ;
        mock.numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts = true;

        return Stream.of(mocks);
    }

    @ParameterizedTest
    @MethodSource("CreateCustomerInfoListWhichReturnInAnAssignableEvaluation")
    @DisplayName("Should be assignable in every Customer Info scenario")
    void shouldBeAssignableForEveryCustomerInfoScenario(List<ICustomerInfo> allAssignableCustomerInfos) {
        var segment = new PrimeSegment();
        var index = 0;

        for (ICustomerInfo customer: allAssignableCustomerInfos) {
            var isAssignable = segment.isCustomerAssignableToSegment(customer);

            assertTrue(isAssignable, "Expected CustomerInfo in index '" + index + "' be assignable, check how the mock has been set.");

            index++;
        }
    }


    @ParameterizedTest
    @MethodSource("CreateAllCustomerInfoWhichReturnNotInAnAssignableEvaluation")
    @DisplayName("Should be assignable in every Customer Info scenario")
    void shouldNotBeAssignableForEveryCustomerInfoScenario(List<ICustomerInfo> allAssignableCustomerInfos) {
        var segment = new PrimeSegment();
        var index = 0;

        for (ICustomerInfo customer: allAssignableCustomerInfos) {
            var isAssignable = segment.isCustomerAssignableToSegment(customer);

            assertFalse(isAssignable, "Expected CustomerInfo in index '" + index + "' be assignable, check how the mock has been set.");

            index++;
        }
    }

    static Stream<List<ICustomerInfo>> CreateCustomerInfoListWhichReturnInAnAssignableEvaluation(){
        var mocks = new ArrayList<ICustomerInfo>();

        CustomerInfo mock = new CustomerInfo();
        var data = new AffordabilityDelphiDataResponse();

        data.setNdspcii(new DataSegmentResponse("NDSPCII","39"));
        data.setE1b07(new DataSegmentResponse("E1B07","U"));
        data.setE1b08(new DataSegmentResponse("E1B08","N"));
        data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
        data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
        data.setE1a07(new DataSegmentResponse("E1A07","1"));
        data.setE1d02(new DataSegmentResponse("E1d02","1"));
        data.setE1b09(new DataSegmentResponse("E1B09","1"));
        data.setNdecc03(new DataSegmentResponse("NDECC03","1"));
        mock.setDelphiBlock(data);
        mocks.add(mock);

        mock = new CustomerInfo();
        data = new AffordabilityDelphiDataResponse();

        data.setNdspcii(new DataSegmentResponse("NDSPCII","20"));
        data.setE1b07(new DataSegmentResponse("E1B07","U"));
        data.setE1b08(new DataSegmentResponse("E1B08","N"));
        data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
        data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
        data.setE1a07(new DataSegmentResponse("E1A07","5"));
        data.setE1d02(new DataSegmentResponse("E1d02","1"));
        data.setE1b09(new DataSegmentResponse("E1B09","4"));
        data.setNdecc03(new DataSegmentResponse("NDECC03","2"));
        mock.setDelphiBlock(data);
        mocks.add(mock);

        mock = new CustomerInfo();
        data = new AffordabilityDelphiDataResponse();

        data.setNdspcii(new DataSegmentResponse("NDSPCII","10"));
        data.setE1b07(new DataSegmentResponse("E1B07","U"));
        data.setE1b08(new DataSegmentResponse("E1B08","N"));
        data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
        data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
        data.setE1a07(new DataSegmentResponse("E1A07","3"));
        data.setE1d02(new DataSegmentResponse("E1d02","1"));
        data.setE1b09(new DataSegmentResponse("E1B09","2"));
        data.setNdecc03(new DataSegmentResponse("NDECC03","1"));
        mock.setDelphiBlock(data);
        mocks.add(mock);


        mock = new CustomerInfo();
        data = new AffordabilityDelphiDataResponse();

        data.setNdspcii(new DataSegmentResponse("NDSPCII","-2"));
        data.setE1b07(new DataSegmentResponse("E1B07","U"));
        data.setE1b08(new DataSegmentResponse("E1B08","N"));
        data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
        data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
        data.setE1a07(new DataSegmentResponse("E1A07","2"));
        data.setE1d02(new DataSegmentResponse("E1d02","2"));
        data.setE1b09(new DataSegmentResponse("E1B09","2"));
        data.setNdecc03(new DataSegmentResponse("NDECC03","1"));
        mock.setDelphiBlock(data);
        mocks.add(mock);

        return Stream.of(mocks);
    }




    static Stream<List<ICustomerInfo>> CreateAllCustomerInfoWhichReturnNotInAnAssignableEvaluation(){
        var mocks = new ArrayList<ICustomerInfo>();

        CustomerInfo mock = new CustomerInfo();
        AffordabilityDelphiDataResponse data = new AffordabilityDelphiDataResponse();

        data.setNdspcii(new DataSegmentResponse("NDSPCII","40"));
        data.setE1b07(new DataSegmentResponse("E1B07","U"));
        data.setE1b08(new DataSegmentResponse("E1B08","N"));
        data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
        data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
        data.setE1a07(new DataSegmentResponse("E1A07","1"));
        data.setE1d02(new DataSegmentResponse("E1d02","1"));
        data.setE1b09(new DataSegmentResponse("E1B09","1"));
        data.setNdecc03(new DataSegmentResponse("NDECC03","1"));
        mock.setDelphiBlock(data);
        mocks.add(mock);

        mock = new CustomerInfo();
        data = new AffordabilityDelphiDataResponse();

        data.setNdspcii(new DataSegmentResponse("NDSPCII","41"));
        data.setE1b07(new DataSegmentResponse("E1B07","U"));
        data.setE1b08(new DataSegmentResponse("E1B08","N"));
        data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
        data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
        data.setE1a07(new DataSegmentResponse("E1A07","5"));
        data.setE1d02(new DataSegmentResponse("E1d02","1"));
        data.setE1b09(new DataSegmentResponse("E1B09","4"));
        data.setNdecc03(new DataSegmentResponse("NDECC03","2"));
        mock.setDelphiBlock(data);
        mocks.add(mock);

        mock = new CustomerInfo();
        data = new AffordabilityDelphiDataResponse();

        data.setNdspcii(new DataSegmentResponse("NDSPCII","10"));
        data.setE1b07(new DataSegmentResponse("E1B07","U"));
        data.setE1b08(new DataSegmentResponse("E1B08","N"));
        data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
        data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
        data.setE1a07(new DataSegmentResponse("E1A07","3"));
        data.setE1d02(new DataSegmentResponse("E1d02","1"));
        data.setE1b09(new DataSegmentResponse("E1B09","2"));
        data.setNdecc03(new DataSegmentResponse("NDECC03","3"));
        mock.setDelphiBlock(data);
        mocks.add(mock);

        return Stream.of(mocks);
    }


}
