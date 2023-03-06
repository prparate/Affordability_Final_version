package com.affordability.customer.segmentation.segments;

import com.affordability.customer.segmentation.ECustomerSegment;
import com.affordability.customer.segmentation.ICustomerInfo;

public interface ICustomerSegment {
    ECustomerSegment getIdentifier();
    boolean isCustomerAssignableToSegment(ICustomerInfo customerInfo);
}
