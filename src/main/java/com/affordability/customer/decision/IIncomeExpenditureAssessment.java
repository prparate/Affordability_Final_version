package com.affordability.customer.decision;

import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import com.affordability.model.EStatus;

import java.math.BigDecimal;


public interface IIncomeExpenditureAssessment {

    public EStatus checkOnScheme(BigDecimal remainingIncome);
    public BigDecimal calculate(ConsumerSummaryDelphiDataResponse consumerSummary, BigDecimal agrInstalment);


}

