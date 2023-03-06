package com.affordability.customer.segmentation;

import com.affordability.customer.segmentation.segments.ICustomerSegment;
import com.affordability.customer.segmentation.segments.ICustomerSegmentationFactory;

import java.util.ArrayList;
import java.util.List;

public class CustomerSegmentationFactoryMock implements ICustomerSegmentationFactory {

    private final CustomerSegmentMock nonPrimeSegment = new CustomerSegmentMock(false, ECustomerSegment.NON_PRIME);
    private List<ICustomerSegment> segments = null;

    public void cleanSegments() {
        this.segments = new ArrayList<>();
    }

    public void AddSegment(boolean isAssignable, ECustomerSegment identifier) {
        CustomerSegmentMock segment = new CustomerSegmentMock(isAssignable, identifier);

        this.segments.add(segment);
    }

    @Override
    public List<ICustomerSegment> getAllSegmentsOrderedByPriority() {
        return this.segments;
    }

    @Override
    public ICustomerSegment getNonPrimeSegment() {
        return nonPrimeSegment;
    }

    public void initialize(ECustomerSegment[] segments) {
        this.cleanSegments();

        Integer index = 0;

        for (var customerSegment: segments) {
            this.AddSegment(true, customerSegment);
            index++;
        }
    }
}
