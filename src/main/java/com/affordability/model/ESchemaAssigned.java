package com.affordability.model;

public enum ESchemaAssigned {
    YES("Yes"),
    NO("No"),
    ERROR("Error");

    private final String text;

    ESchemaAssigned(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

}
