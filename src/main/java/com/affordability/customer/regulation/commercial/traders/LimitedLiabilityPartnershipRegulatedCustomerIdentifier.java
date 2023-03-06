package com.affordability.customer.regulation.commercial.traders;

import com.affordability.customer.ETraderType;
import com.affordability.customer.regulation.CustomerDataForDetermineRegulation;
import com.affordability.customer.regulation.commercial.ICommercialRegulatedCustomerIdentifier;

public class LimitedLiabilityPartnershipRegulatedCustomerIdentifier implements ICommercialRegulatedCustomerIdentifier {

    @Override
    public ETraderType getTraderType() {
        return ETraderType.LIMITED_LIABILITY_PARTNERSHIP;
    }

    @Override
    public void setCustomerDataForRegulation(CustomerDataForDetermineRegulation data) {
        //LimitedLiabilityPartnershipRegulatedCustomerIdentifier does not support this operation
    }

    @Override
    public boolean isThisARegulatedCustomer() {
        return false;
    }

}
