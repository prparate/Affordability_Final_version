package com.affordability.customer.segmentation;

import com.affordability.customer.segmentation.segments.ICustomerSegment;

public interface ICustomerSegmentationAssigner {

    ICustomerSegment determineSegment(ICustomerInfo customerInfo);
}
