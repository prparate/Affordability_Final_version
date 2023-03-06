package com.affordability.customer.regulation;

import com.affordability.customer.ETraderType;
import com.affordability.model.EBusinessLine;
import com.affordability.model.EIncorporationStatus;
import lombok.Getter;
import org.apache.commons.lang3.StringUtils;

import java.math.BigDecimal;

@Getter
public class CustomerDataForDetermineRegulation {
    private EBusinessLine businessLine = null;
    private ETraderType traderType = null;
    private BigDecimal policyValue = null;

    private EIncorporationStatus incorporationStatus = EIncorporationStatus.ASSUMED_UNINCORPORATED;
    private int numberOfPartners = 0;

    public void setBusinessLine(String personalOrCorporate) {

        if (StringUtils.equalsIgnoreCase(personalOrCorporate, "C")) {
            this.businessLine = EBusinessLine.COMMERCIAL;
        } else {
            this.businessLine = EBusinessLine.PERSONAL;
        }
    }


    public void setTraderTypeByCode(String traderTypeCode) {
        var trader = ETraderType.getTraderTypeByCodeOrDefault(traderTypeCode);
        this.traderType = trader;
    }
    public void setPolicyValue(BigDecimal policyValue) {
        this.policyValue = policyValue;
    }

    public void setNumberOfPartners(int numberOfPartners) {
        this.numberOfPartners = numberOfPartners;
    }

    public boolean policyValueIsEqualOrLessThan(double value) {
        final var maxPolicyValue =  BigDecimal.valueOf(value);

        var result = this.getPolicyValue().compareTo(maxPolicyValue);
        return result <= 0;
    }

    public boolean numberOfPartnersAreEqualOrLessThan(int maxNumberOfPartners) {
        var totalPartners = this.getNumberOfPartners();
        return totalPartners <= maxNumberOfPartners;
    }
}
