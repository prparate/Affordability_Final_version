package com.affordability.model;

public enum EIncorporationStatus {
    INCORPORATED("incorporated"),
    UNINCORPORATED("unincorporated"),
    ASSUMED_UNINCORPORATED("assumed_unincorporated");

    private final String value;

    private EIncorporationStatus(String value){
        this.value = value;
    }
}
