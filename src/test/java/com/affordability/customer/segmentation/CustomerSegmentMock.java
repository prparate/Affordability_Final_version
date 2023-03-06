package com.affordability.customer.segmentation;

import com.affordability.customer.segmentation.segments.ICustomerSegment;

public class CustomerSegmentMock implements ICustomerSegment {

    private final ECustomerSegment identifier;
    private final boolean isAssignable;

    public CustomerSegmentMock(boolean isAssignable, ECustomerSegment identifier) {
        this.isAssignable = isAssignable;
        this.identifier = identifier;
    }

    @Override
    public ECustomerSegment getIdentifier() {
        return identifier;
    }

    @Override
    public boolean isCustomerAssignableToSegment(ICustomerInfo customerInfo) {
        return isAssignable;
    }

}
