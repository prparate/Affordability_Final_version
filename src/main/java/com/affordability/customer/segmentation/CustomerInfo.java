package com.affordability.customer.segmentation;

import com.affordability.dto.response.AffordabilityDelphiDataResponse;

import java.util.Arrays;

public class CustomerInfo implements ICustomerInfo{
    private AffordabilityDelphiDataResponse delphiBlock;
    private String[] last6MonthsWorstStatus =  {"0","1","N","U"};
    private static final int G4NDSPCII_ACCEPTANCE_CRITERIA2 = -2;


    public void setDelphiBlock(AffordabilityDelphiDataResponse delphiBlock) {
        this.delphiBlock = delphiBlock;
    }

    public boolean enhancedCustomerIndebtednessIndex() {
        var g4ndspcii = this.delphiBlock.getNdspcii();

        return (g4ndspcii == null || g4ndspcii.isValueNullOrEmpty() || g4ndspcii.isValueEqual("-1"));
    }

    public boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsLessThanPaymentDown() {
        var e1b07 = this.delphiBlock.getE1b07();

        return (e1b07 == null || e1b07.isValueNullOrEmpty() || e1b07.isValueEqual("T"));
    }

    public boolean worstCurrentStatusOfAllActiveCAISAccountsIsLessThan2PaymentDown() {
        var e1b08 = this.delphiBlock.getE1b08();

        return (e1b08 == null || e1b08.isValueNullOrEmpty() || e1b08.isValueEqual("T"));
    }

    public boolean detectedCCJsOrIVAOrBankruptcyInLast6Year() {
        var ea1c01 = this.delphiBlock.getEa1c01();

        return (ea1c01 == null ||ea1c01.isValueNullOrEmpty() || ea1c01.isValueEqual("T"));

    }

    public boolean enhancedCustomerIndebtednessIndexForNonPrime() {

        var g4ndspcii = this.delphiBlock.getNdspcii();

        if(g4ndspcii != null && g4ndspcii.isValueAnInteger()) {
            var g4ndspciiValue = g4ndspcii.getValueAsInteger();
            return (g4ndspciiValue >= 40 || g4ndspciiValue == -3);
        }
        return false;
    }

    public boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime() {

        var e1b07 = this.delphiBlock.getE1b07();

        if(e1b07 != null && e1b07.isValueAnInteger()){
            var e1b07Value = e1b07.getValueAsInteger();
            return (e1b07Value > 1);
        }
        return false;
    }

    public boolean worstCurrentStatusOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime() {

        var e1b08 = this.delphiBlock.getE1b08();

        if(e1b08 != null && e1b08.isValueAnInteger()){
            var e1b08Value = e1b08.getValueAsInteger();
            return (e1b08Value > 1);
        }
        return false;
    }

    public boolean detectedCCJsOrIVAOrBankruptcyInLast6YearforNonPrime() {

        var ea1c01 = this.delphiBlock.getEa1c01();

        if(ea1c01 != null && !ea1c01.isValueNullOrEmpty()){
            var ea1c01Value = ea1c01.getValue();
            return (ea1c01Value.equalsIgnoreCase("Y"));
        }
        return false;
    }

    public boolean enhancedCustomerIndebtednessIndexForPrime() {
        var g4ndspcii = this.delphiBlock.getNdspcii();
        var isValidValue = g4ndspcii != null && g4ndspcii.isValueAnInteger();

        if (isValidValue) {
            var g4ndspciiVal = g4ndspcii.getValueAsInteger();
            return ((0 <= g4ndspciiVal && g4ndspciiVal < 40)|| G4NDSPCII_ACCEPTANCE_CRITERIA2 == g4ndspciiVal);
        }
        return false;
    }

    public boolean worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime() {
        var e1b07 = this.delphiBlock.getE1b07() ;

        if ( e1b07 != null)
            return ( (Arrays.binarySearch(last6MonthsWorstStatus,  this.delphiBlock.getE1b07().getValue())) >= 0 );
        return false;
    }

    public boolean detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime() {

        var ea1c01 = this.delphiBlock.getEa1c01();

        if (ea1c01 != null && !ea1c01.isValueNullOrEmpty()) {
            var ea1c01Value = ea1c01.getValue();
            return (ea1c01Value.equalsIgnoreCase("N"));
        }
        return false;
    }

    public boolean settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin() {

        int e1a07 = (this.delphiBlock.getE1a07() != null ) ? this.delphiBlock.getE1a07().getValueAsIntegerOrDefault() : 0;
        int e1d02 = (this.delphiBlock.getE1d02() != null)  ? this.delphiBlock.getE1d02().getValueAsIntegerOrDefault() : 0;
        int e1b09 = (this.delphiBlock.getE1b09() != null) ? this.delphiBlock.getE1b09().getValueAsIntegerOrDefault() : 0;

        return (e1a07 + e1d02 + e1b09) >= 3;

    }

    public boolean numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts() {

        if (this.delphiBlock.getNdecc03() == null || this.delphiBlock.getE1b09()== null)
            return false;

        if (!this.delphiBlock.getNdecc03().isValueAnInteger()  || !this.delphiBlock.getE1b09().isValueAnInteger())
            return false;

        return this.delphiBlock.getE1b09().getValueAsInteger() >= this.delphiBlock.getNdecc03().getValueAsInteger();
    }


}
