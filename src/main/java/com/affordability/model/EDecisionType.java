package com.affordability.model;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum EDecisionType {
    MANUAL("Manual"),
    AUTOMATIC("Automatic");
    private final String text;

    EDecisionType(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    private static Map<String, EDecisionType> nameToValue;
    static {
        nameToValue = Stream.of(values()).collect(Collectors.toMap(EDecisionType::getText, Function.identity()));
    }
}
