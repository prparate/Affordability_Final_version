package com.affordability.customer.segmentation;

import com.affordability.model.AffordabilityDelphi;

public class CustomerInfoMock implements  ICustomerInfo {

    public boolean enhancedCustomerIndebtednessIndexReturn;
    public boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsLessThanPaymentDownReturn;
    public boolean worstCurrentStatusOfAllActiveCAISAccountsIsLessThan2PaymentDownReturn;
    public boolean detectedCCJsOrIVAOrBankruptcyInLast6YearReturn;
    public boolean enhancedCustomerIndebtednessIndexForNonPrimeReturn;
    public boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrimeReturn;
    public boolean worstCurrentStatusOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrimeReturn;
    public boolean detectedCCJsOrIVAOrBankruptcyInLast6YearforNonPrimeReturn;
    public boolean enhancedCustomerIndebtednessIndexForPrime;
    public boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime;
    public boolean detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime;
    public boolean settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin;
    public boolean numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts;

    @Override
    public boolean enhancedCustomerIndebtednessIndex() {
        return enhancedCustomerIndebtednessIndexReturn;
    }

    @Override
    public boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsLessThanPaymentDown() {
        return worstStatusInLast6MonthsOfAllActiveCAISAccountsIsLessThanPaymentDownReturn;
    }

    @Override
    public boolean worstCurrentStatusOfAllActiveCAISAccountsIsLessThan2PaymentDown() {
        return worstCurrentStatusOfAllActiveCAISAccountsIsLessThan2PaymentDownReturn;
    }

    @Override
    public boolean detectedCCJsOrIVAOrBankruptcyInLast6Year() {
        return detectedCCJsOrIVAOrBankruptcyInLast6YearReturn;
    }

    @Override
    public boolean enhancedCustomerIndebtednessIndexForNonPrime() {
        return enhancedCustomerIndebtednessIndexForNonPrimeReturn;
    }

    @Override
    public boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime() {
        return worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrimeReturn;
    }

    @Override
    public boolean worstCurrentStatusOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime() {
        return worstCurrentStatusOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrimeReturn;
    }

    @Override
    public boolean detectedCCJsOrIVAOrBankruptcyInLast6YearforNonPrime() {
        return detectedCCJsOrIVAOrBankruptcyInLast6YearforNonPrimeReturn;
    }

    @Override
    public boolean enhancedCustomerIndebtednessIndexForPrime() {
        return enhancedCustomerIndebtednessIndexForPrime;
    }

    @Override
    public boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime() {
        return worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime;
    }

    @Override
    public boolean detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime() {
        return detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime;
    }

    @Override
    public boolean settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin() {
        return settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin;
    }

    @Override
    public boolean numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts() {
        return numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts;
    }
    private AffordabilityDelphi delphiBlock;

    public AffordabilityDelphi getDelphiBlock() {
        return delphiBlock;
    }

    public void setDelphiBlock(AffordabilityDelphi delphiBlock) {
        this.delphiBlock = delphiBlock;
    }
}
