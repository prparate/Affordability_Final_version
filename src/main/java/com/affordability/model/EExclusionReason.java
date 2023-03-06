package com.affordability.model;

public enum EExclusionReason {
    UN_REGULATED("Un Regulated"),
    UN_FUNDED("Un Funded");
    private final String text;

    EExclusionReason(final String text) {
        this.text = text;
    }
    @Override
    public String toString() {
        return text;
    }
}
