package com.affordability.customer.regulation.personal;

import com.affordability.customer.regulation.CustomerDataForDetermineRegulation;
import com.affordability.customer.regulation.IRegulatedCustomerIdentifier;

public class RegulatedPersonalCustomerIdentifier implements IRegulatedCustomerIdentifier {

    @Override
    public boolean isThisARegulatedCustomer() {
        return true;
    }

    @Override
    public void setCustomerDataForRegulation(CustomerDataForDetermineRegulation data) {
        //RegulatedPersonalCustomerIdentifier does not support this operation
    }
}
