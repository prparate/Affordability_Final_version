package com.affordability.customer.decision;


import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import com.affordability.model.EStatus;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class IncomeExpenditureAssessment implements IIncomeExpenditureAssessment {

    public static final BigDecimal PERCENT_FACTOR = BigDecimal.valueOf(0.89);

    @Override
    public EStatus checkOnScheme(BigDecimal remainingIncome) {
        if (remainingIncome == null || remainingIncome.compareTo(BigDecimal.ZERO) < 0) {
            return EStatus.ON_SCHEME;
        }

        return EStatus.NOT_ON_SCHEME;
    }

    public BigDecimal calculate(ConsumerSummaryDelphiDataResponse consumerSummary, BigDecimal agrInstalment) {
        if (consumerSummary.hasNullNegativeOrInvalidValue())
            return null;

        var monthlyIncome = BigDecimal.ZERO;
        var mortgageOrRent = BigDecimal.ZERO;
        var essentialExpenditure = BigDecimal.ZERO;
        var creditCommitments = BigDecimal.ZERO;

        if (consumerSummary.getSpedi03() != null)
            monthlyIncome = consumerSummary.getSpedi03().getValueAsBigDecimal();
        if (consumerSummary.getSpedi04() != null)
            mortgageOrRent = consumerSummary.getSpedi04().getValueAsBigDecimal();
        if (consumerSummary.getSpedi05() != null)
            essentialExpenditure = consumerSummary.getSpedi05().getValueAsBigDecimal();
        if (consumerSummary.getSpedi08() != null)
            creditCommitments = consumerSummary.getSpedi08().getValueAsBigDecimal();

        BigDecimal expenditure =  mortgageOrRent.add(creditCommitments);
        expenditure = expenditure.add(agrInstalment);
        expenditure = expenditure.add(essentialExpenditure.multiply(PERCENT_FACTOR));

        return monthlyIncome.subtract(expenditure);
    }

}
