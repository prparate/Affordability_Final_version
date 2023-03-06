package com.affordability.model;

public enum EAutomatedDecision {
    PASS("Pass"),
    FAIL("Fail"),
    ERROR("Error");

    private final String text;

    EAutomatedDecision(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
