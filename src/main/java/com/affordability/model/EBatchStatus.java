package com.affordability.model;

import java.util.stream.Stream;

public enum EBatchStatus {
    PENDING,
    PROCESSING,
    COMPLETED,
    FAILED;

    public static EBatchStatus getEnumByString(String value) {
        return Stream.of(values())
                .filter(e -> e.name().equals(value))
                .findFirst()
                .orElseThrow( () -> new IllegalArgumentException("Invalid value for EBatchStatus: " + value));
    }
}
