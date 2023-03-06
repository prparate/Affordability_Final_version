package com.affordability.model;

public enum EInclusionOutcome {
    INCLUSION("Inclusion"),
    EXCLUSION("Exclusion")
    ;

    private final String text;

    EInclusionOutcome(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }
}
