package com.affordability.customer.segmentation;

import com.affordability.customer.segmentation.segments.NotClassifiedSegment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class NotClassifiedSegmentTest {

    @ParameterizedTest
    @MethodSource("CreateAllCustomerInfoMocksWhichReturnInAnAssignableEvaluation")
    @DisplayName("Should be assignable in every Customer Info scenario")
    void shouldBeAssignableInEveryCustomerInfoScenario(List<ICustomerInfo> allAssignableCustomerInfos) {
        var segment = new NotClassifiedSegment();
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
        mock.enhancedCustomerIndebtednessIndexReturn = true;
        mocks.add(mock);

        mock = new CustomerInfoMock();
        mock.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsLessThanPaymentDownReturn = true;
        mocks.add(mock);

        mock = new CustomerInfoMock();
        mock.worstCurrentStatusOfAllActiveCAISAccountsIsLessThan2PaymentDownReturn = true;
        mocks.add(mock);

        mock = new CustomerInfoMock();
        mock.detectedCCJsOrIVAOrBankruptcyInLast6YearReturn = true;
        mocks.add(mock);

        return Stream.of(mocks);
    }
}
