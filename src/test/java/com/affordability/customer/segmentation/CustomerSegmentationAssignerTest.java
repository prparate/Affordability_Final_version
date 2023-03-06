package com.affordability.customer.segmentation;

import com.affordability.customer.segmentation.segments.ICustomerSegment;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class CustomerSegmentationAssignerTest {

    private final CustomerSegmentationFactoryMock segmentationFactoryMock = new CustomerSegmentationFactoryMock();

    @Test
    @DisplayName("Should return an assignable segment")
    void shouldReturnAnAssignableSegment() {
        Integer indexOfAssignableSegment = 0;
        var customerSegments=new ECustomerSegment[]{
                GetAssignableSegment(),
                GetNotAssignableSegment(),
        };
        this.initializeSegments(customerSegments);

        var assignableSegment = this.findAssignableSegment();

        assertNotNull(assignableSegment);
        assertEquals(customerSegments[indexOfAssignableSegment], assignableSegment.getIdentifier());
    }

    @Test
    @DisplayName("Should return NullSegment when there is no assignable segment")
    void shouldReturnNonPrimeSegmentWhenThereIsNoAssignableSegment() {

        var customerSegments=new ECustomerSegment[]{
                GetNotAssignableSegment(),
                GetNotAssignableSegment(),
                GetNotAssignableSegment(),
        };

        this.initializeSegments(customerSegments);

        var assignableSegment = this.findAssignableSegment();

        assertNotNull(assignableSegment);

        var expectedNonPrimeSegment = this.segmentationFactoryMock.getNonPrimeSegment();
        assertEquals(expectedNonPrimeSegment.getIdentifier(), assignableSegment.getIdentifier());
    }

    @Test
    @DisplayName("Should return the first assignable segment when there are many assignable segments")
    void shouldReturnTheFirstAssignableSegmentWhenThereAreManyAssignableSegments() {
        Integer firstAssignableSegmentIndex = 0;

        var customerSegments=new ECustomerSegment[]{
                GetAssignableSegment(),
                GetNotAssignableSegment(),
                GetAssignableSegment(),
                GetNotAssignableSegment(),
                GetNotAssignableSegment(),
                GetNotAssignableSegment(),
            };
        this.initializeSegments(customerSegments);

        var assignableSegment = this.findAssignableSegment();
        var identifier = assignableSegment.getIdentifier();

        assertNotNull(assignableSegment);
        assertEquals(customerSegments[firstAssignableSegmentIndex], identifier);
    }

    private ECustomerSegment GetNotAssignableSegment() {
        return ECustomerSegment.NON_PRIME;
    }

    private ECustomerSegment GetAssignableSegment() {
        return ECustomerSegment.PRIME;
    }

    private ICustomerSegment findAssignableSegment() {
        var assigner = new CustomerSegmentationAssigner(this.segmentationFactoryMock);

        var assignableSegment = assigner.determineSegment(new CustomerInfo());

        return assignableSegment;
    }

    private void initializeSegments(ECustomerSegment[] assignabilityList) {
        segmentationFactoryMock.initialize(assignabilityList);
    }
}
