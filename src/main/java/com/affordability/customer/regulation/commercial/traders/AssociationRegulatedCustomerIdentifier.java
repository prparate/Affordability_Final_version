package com.affordability.customer.regulation.commercial.traders;

import com.affordability.customer.ETraderType;
import com.affordability.customer.regulation.CustomerDataForDetermineRegulation;
import com.affordability.customer.regulation.commercial.ICommercialRegulatedCustomerIdentifier;
import com.affordability.customer.regulation.parameter.IRegulatedCustomerParameterProvider;

public class AssociationRegulatedCustomerIdentifier implements ICommercialRegulatedCustomerIdentifier {

    private final IRegulatedCustomerParameterProvider parameterProvider;
    private CustomerDataForDetermineRegulation data;

    public AssociationRegulatedCustomerIdentifier(IRegulatedCustomerParameterProvider parameterProvider) {
        this.parameterProvider = parameterProvider;
    }

    @Override
    public ETraderType getTraderType() {
        return ETraderType.ASSOCIATION;
    }

    @Override
    public void setCustomerDataForRegulation(CustomerDataForDetermineRegulation data) {
        this.data = data;
    }

    @Override
    public boolean isThisARegulatedCustomer() {
        var maxPolicy = parameterProvider.getMaxPolicyValueAllowed();

        return this.data.policyValueIsEqualOrLessThan(maxPolicy);
    }
}
