package com.affordability.customer.regulated;

import com.affordability.customer.regulation.parameter.IRegulatedCustomerParameterProvider;

public class RegulatedCustomerParameterProviderMock implements IRegulatedCustomerParameterProvider {

    public double getMaxPolicyValueAllowedReturn;
    public int getMaxNumberOfPartnersAllowedReturn;

    @Override
    public double getMaxPolicyValueAllowed() {
        return getMaxPolicyValueAllowedReturn;
    }

    @Override
    public int getMaxNumberOfPartnersAllowed() {
        return getMaxNumberOfPartnersAllowedReturn;
    }
}
