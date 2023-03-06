package com.affordability.customer.segmentation.segments;

import com.affordability.customer.segmentation.ECustomerSegment;
import com.affordability.customer.segmentation.ICustomerInfo;

public class NonPrimeSegment implements ICustomerSegment{
	@Override
	public ECustomerSegment getIdentifier() {
		return ECustomerSegment.NON_PRIME;
	}

	@Override
	public boolean isCustomerAssignableToSegment(ICustomerInfo customerInfo) {
		return customerInfo.enhancedCustomerIndebtednessIndexForNonPrime()
				|| customerInfo.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime()
				|| customerInfo.worstCurrentStatusOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime()
				|| customerInfo.detectedCCJsOrIVAOrBankruptcyInLast6YearforNonPrime();
	}

}
