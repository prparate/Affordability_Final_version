package com.affordability.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AffordabilityDelphi implements Serializable {
    @JsonProperty("NDSPCII")
    private String ndspcii;

    @JsonProperty("E1B07")
    private String e1b07;

    @JsonProperty("E1B08")
    private String e1b08;

    @JsonProperty("EA1C01")
    private String ea1c01;

    @JsonProperty("EA4Q05")
    private String ea4q05;

    @JsonProperty("E1A07")
    private String e1a07;

    @JsonProperty("E1D02")
    private String e1d02;

    @JsonProperty("E1B09")
    private String e1b09;

    @JsonProperty("NDECC03")
    private String ndecc03;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    public Instant requestedOn;
    public String uuidInstance;

}