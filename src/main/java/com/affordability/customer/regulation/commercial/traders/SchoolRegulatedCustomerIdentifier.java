package com.affordability.customer.regulation.commercial.traders;

import com.affordability.customer.ETraderType;
import com.affordability.customer.regulation.CustomerDataForDetermineRegulation;
import com.affordability.customer.regulation.commercial.ICommercialRegulatedCustomerIdentifier;

public class SchoolRegulatedCustomerIdentifier implements ICommercialRegulatedCustomerIdentifier {

    @Override
    public ETraderType getTraderType() {
        return ETraderType.SCHOOL;
    }

    @Override
    public void setCustomerDataForRegulation(CustomerDataForDetermineRegulation data) {
        //SchoolRegulatedCustomerIdentifier does not support this operation
    }

    @Override
    public boolean isThisARegulatedCustomer() {
        return false;
    }
}
