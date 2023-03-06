package com.affordability.customer.segmentation.segments;

import java.util.List;

public interface ICustomerSegmentationFactory {
    List<ICustomerSegment> getAllSegmentsOrderedByPriority();

    ICustomerSegment getNonPrimeSegment();
}
