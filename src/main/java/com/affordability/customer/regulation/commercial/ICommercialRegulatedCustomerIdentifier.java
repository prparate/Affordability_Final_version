package com.affordability.customer.regulation.commercial;

import com.affordability.customer.ETraderType;
import com.affordability.customer.regulation.IRegulatedCustomerIdentifier;

public interface ICommercialRegulatedCustomerIdentifier extends IRegulatedCustomerIdentifier {
    ETraderType getTraderType();
}
