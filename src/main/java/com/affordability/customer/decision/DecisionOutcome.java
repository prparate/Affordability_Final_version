package com.affordability.customer.decision;

import com.affordability.model.EDecisionType;
import com.affordability.model.ELowInstalment;
import com.affordability.model.EStatus;
import lombok.Getter;
import lombok.ToString;

import java.math.BigDecimal;

@Getter
@ToString
public class DecisionOutcome {
    public EStatus status;
    public String effectiveDisposableSurplus;
    public EDecisionType decisionType;
    public ELowInstalment lowInstalment;
    public String incomeExpenditure;
}
