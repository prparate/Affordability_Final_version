package com.affordability.customer.decision;

import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;

import java.math.BigDecimal;

public interface IEffectiveDisposableSurplus {
    BigDecimal getEffectiveDisposableSurplus(ConsumerSummaryDelphiDataResponse consumerSummary);
}
