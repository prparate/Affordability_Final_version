package com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataForDetermineRegulationQueryResponse {
    public String agreementNumber;
    public String traderCode;
    public String traderType;
    public String personalOrCorporate;
    public BigDecimal advance;
    public Integer numberOfPartners;
}
