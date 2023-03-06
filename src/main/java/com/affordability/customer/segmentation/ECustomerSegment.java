package com.affordability.customer.segmentation;

public enum ECustomerSegment {
    NOT_CLASSIFIED("NOT CLASSIFIED"),
    NON_PRIME("NON PRIME"),
    PRIME("PRIME");

    private final String text;

    ECustomerSegment(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
