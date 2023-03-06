package com.affordability.service.cra.request;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies.SnakeCaseStrategy;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;


@NoArgsConstructor
@Getter
@Setter
@JsonNaming(SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class CraRequest {
    public String agreementNumber;
    public String personNameFirst;
    public String personNameMiddle;
    public String personNameLast;
    public String personDateOfBirth;
    public String bankAccountNumber;
    public String bankAccountSortCode;
    public String personCountry;
    public String personCountyCode;
    public String personHouseName;
    public String personHouseNumber;
    public String personLocality;
    public String personPostCode;
    public String personPostTown;
    public String personStreetName;
    public String personStreetType;
    public String personTitle;
    public BigDecimal agrAdvance;
    public String habitationDateIn;
    public String habitationDateOut;
}
