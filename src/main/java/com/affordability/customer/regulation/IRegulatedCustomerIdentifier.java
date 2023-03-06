package com.affordability.customer.regulation;

public interface IRegulatedCustomerIdentifier {
    void setCustomerDataForRegulation(CustomerDataForDetermineRegulation data);
    boolean isThisARegulatedCustomer();
}
