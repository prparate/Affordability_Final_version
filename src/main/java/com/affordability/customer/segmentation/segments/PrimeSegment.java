package com.affordability.customer.segmentation.segments;

import com.affordability.customer.segmentation.ECustomerSegment;
import com.affordability.customer.segmentation.ICustomerInfo;

public class PrimeSegment implements ICustomerSegment {

    @Override
    public ECustomerSegment getIdentifier() {
        return ECustomerSegment.PRIME;
    }


    @Override
    public boolean isCustomerAssignableToSegment(ICustomerInfo customerInfo) {
        return customerInfo.enhancedCustomerIndebtednessIndexForPrime()
                && customerInfo.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime()
                && customerInfo.detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime()
                && customerInfo.settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin()
                && customerInfo.numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts();
    }
}
