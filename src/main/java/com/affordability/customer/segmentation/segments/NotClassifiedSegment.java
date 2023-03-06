package com.affordability.customer.segmentation.segments;

import com.affordability.customer.segmentation.ECustomerSegment;
import com.affordability.customer.segmentation.ICustomerInfo;

public class NotClassifiedSegment implements ICustomerSegment {

    @Override
    public ECustomerSegment getIdentifier() {
        return ECustomerSegment.NOT_CLASSIFIED;
    }

    @Override
    public boolean isCustomerAssignableToSegment(ICustomerInfo customerInfo) {
        return customerInfo.enhancedCustomerIndebtednessIndex()
                    || customerInfo.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsLessThanPaymentDown()
                    || customerInfo.worstCurrentStatusOfAllActiveCAISAccountsIsLessThan2PaymentDown()
                    || customerInfo.detectedCCJsOrIVAOrBankruptcyInLast6Year();
    }
}
