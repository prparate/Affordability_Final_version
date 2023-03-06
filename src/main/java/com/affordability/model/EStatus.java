package com.affordability.model;

import lombok.Getter;

import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Getter
public enum EStatus {
    ON_SCHEME("On Scheme"),
    NOT_ON_SCHEME("Not On Scheme"),
    NOT_CHECKED("Not Checked");

    private final String text;

    EStatus(final String text) {
        this.text = text;
    }

    @Override
    public String toString() {
        return text;
    }

    private static Map<String, EStatus> nameToValue;
    static {
        nameToValue = Stream.of(values()).collect(Collectors.toMap(EStatus::getText, Function.identity()));
    }
}
