package com.affordability.customer.segmentation;

public interface ICustomerInfo {
    boolean enhancedCustomerIndebtednessIndex();
    boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsLessThanPaymentDown();
    boolean worstCurrentStatusOfAllActiveCAISAccountsIsLessThan2PaymentDown();
    boolean detectedCCJsOrIVAOrBankruptcyInLast6Year();

    boolean enhancedCustomerIndebtednessIndexForNonPrime();
    boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime() ;
    boolean worstCurrentStatusOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime();
    boolean detectedCCJsOrIVAOrBankruptcyInLast6YearforNonPrime();
    boolean enhancedCustomerIndebtednessIndexForPrime();
    boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime();
    boolean detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime();
    boolean settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin();
    boolean numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts();
}
