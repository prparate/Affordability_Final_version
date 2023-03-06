package com.affordability.service.cra.response;

import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerSummaryResponse {

    private ConsumerSummaryDelphiDataResponse data;
    private String uuidInstance;
    private String datetimeRequest;

}