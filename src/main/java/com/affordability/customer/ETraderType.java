package com.affordability.customer;

import org.apache.commons.lang3.StringUtils;

public enum ETraderType {
    ASSOCIATION("Association", "A"),
    CORPORATE("Corporate", "C"),
    LIMITED_COMPANY("Limited Company", "Y"),
    LIMITED_LIABILITY_PARTNERSHIP("Limited Liability Partnership", "X"),
    LOCAL_AUTHORITY("Local Authority", "L"),
    NHS_TRUST("NHS Trust", "T"),
    PARTNERSHIP("Partnership", "P"),
    PLC("PLC", "Z"),
    REGISTERED_CHARITY("Registered Charity", "R"),
    SCHOOL("School", "E"),
    SOLE_TRADER("Sole Trader", "S"),
    ;

    private final String text;
    private final String code;

    ETraderType(final String text, final String code) {
        this.text = text;
        this.code = code;
    }

    @Override
    public String toString() {
        return text;
    }

    public String getCode() {
        return code;
    }

    public static ETraderType getTraderTypeByCodeOrDefault(String code) {
        if (StringUtils.isBlank(code)) {
            return null;
        }

        ETraderType found = null;

        for (ETraderType trader : ETraderType.values()) {
            if (StringUtils.equalsIgnoreCase(trader.getCode(), code)) {
                found = trader;
                break;
            }
        }

        return found;
    }
}
