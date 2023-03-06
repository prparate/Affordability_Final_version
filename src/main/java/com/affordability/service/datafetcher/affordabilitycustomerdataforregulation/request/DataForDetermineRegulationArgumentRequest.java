package com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.ToString;

@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DataForDetermineRegulationArgumentRequest {
    public String agreementNumber;
}
