package com.affordability.model;

public enum ELowInstalment {
    YES("Yes"),
    NO("No"),
    ERROR("Error");

    private final String text;

    ELowInstalment(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
