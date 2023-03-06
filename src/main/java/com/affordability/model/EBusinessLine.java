package com.affordability.model;

public enum EBusinessLine {
    PERSONAL("Personal"),
    COMMERCIAL("Commercial")
    ;

    private final String text;

    EBusinessLine(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
