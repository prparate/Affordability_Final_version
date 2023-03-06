package com.affordability.customer.regulation;

public class NullRegulatedCustomerIdentifier implements IRegulatedCustomerIdentifier {
    @Override
    public void setCustomerDataForRegulation(CustomerDataForDetermineRegulation data) {
        throw new UnsupportedOperationException("NullRegulatedCustomerIdentifier does not support this operation");
    }

    @Override
    public boolean isThisARegulatedCustomer() {
        return false;
    }
}
