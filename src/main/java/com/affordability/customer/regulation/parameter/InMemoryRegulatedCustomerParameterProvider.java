package com.affordability.customer.regulation.parameter;

import org.springframework.stereotype.Service;

@Service
public class InMemoryRegulatedCustomerParameterProvider implements IRegulatedCustomerParameterProvider {
    @Override
    public double getMaxPolicyValueAllowed() {
        return 25000;
    }

    @Override
    public int getMaxNumberOfPartnersAllowed() {
        return 3;
    }
}
