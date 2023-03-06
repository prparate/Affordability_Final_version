package com.affordability.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.*;

import java.io.Serializable;
import java.time.Instant;

@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerSummaryDelphi implements Serializable {

    @JsonProperty("SPEDI03")
    private String spedi03;

    @JsonProperty("SPEDI04")
    private String spedi04;

    @JsonProperty("SPEDI05")
    private String spedi05;

    @JsonProperty("SPEDI08")
    private String spedi08;

    @JsonFormat(pattern = "yyyy-MM-dd HH:mm:ss", timezone = "UTC")
    private Instant requestedOn;
    private String uuidInstance;

}
