package com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.request;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class DataForDetermineRegulationRequest {
    public DataForDetermineRegulationArgumentRequest arguments;
}
