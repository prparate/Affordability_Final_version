package com.affordability.dto.response;

import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;

import java.math.BigDecimal;
import java.util.Date;

@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
public class AgreementResponse {
    public String number;
    public String serial;
    public Date created;
    public BigDecimal totalPremiumValue;
    public BigDecimal agrInstalment;
}
