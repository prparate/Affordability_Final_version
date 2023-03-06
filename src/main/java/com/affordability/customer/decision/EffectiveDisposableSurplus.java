package com.affordability.customer.decision;


import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
@Slf4j
public class EffectiveDisposableSurplus implements IEffectiveDisposableSurplus {

    @Value("${effective-disposable-surplus.percent-factor}")
    private BigDecimal percentFactor;

    @Override
    public BigDecimal getEffectiveDisposableSurplus(ConsumerSummaryDelphiDataResponse consumerSummary) {
        if (consumerSummary.hasNullNegativeOrInvalidValue()) {
            log.info("Consumer Summary has null or negative values");
            return null;
        }

        var monthlyIncome = consumerSummary.getSpedi03().getValueAsBigDecimal();
        var mortgageOrRent = consumerSummary.getSpedi04().getValueAsBigDecimal();
        var essentialExpenditure = consumerSummary.getSpedi05().getValueAsBigDecimal();
        var creditCommitments = consumerSummary.getSpedi08().getValueAsBigDecimal();


        BigDecimal expenditure = mortgageOrRent.add(creditCommitments);
        expenditure = expenditure.add(essentialExpenditure.multiply(percentFactor));

        return monthlyIncome.subtract(expenditure);

    }


}
