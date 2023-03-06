package com.affordability.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataFetcherResponse {
    private Integer status;
    private String name;
    private String versionCode;
    private String versionApi;
    private String hostname;
    private String path;
    private String uuidInstance;
    private Integer idRequest;
    private LocalDateTime dateTimeRequest;
    private String versionCore;
    private List<AgreementDetail> data;
}
