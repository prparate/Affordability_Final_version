package com.affordability.customer.decision;

import com.affordability.customer.segmentation.ECustomerSegment;
import com.affordability.dto.response.AffordabilityDelphiDataResponse;
import com.affordability.dto.response.AgreementDetail;
import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import com.affordability.model.*;
import com.affordability.service.cra.ICraService;
import com.affordability.service.cra.request.CraRequest;
import com.affordability.service.cra.response.ConsumerSummaryResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

import java.math.BigDecimal;

@Slf4j
@Service
@RequiredArgsConstructor
@RequestScope
public class CustomerDecision implements ICustomerDecision {

    private final IIncomeExpenditureAssessment incomeExpenditureAssessment;

    private final IEffectiveDisposableSurplus effectiveDisposableSurplus;

    public final ICraService craService;

    private static final BigDecimal INSTALMENT_LIMIT = BigDecimal.valueOf(56);

    private DecisionOutcome decisionOutcome;

    private ConsumerSummaryResponse consumerSummary = null;

	public DecisionOutcome processCustomerSegment(ECustomerSegment customerSegment, AffordabilityDelphiDataResponse delphiDataResponse, CraRequest craRequest, AgreementDetail agreementDetail) throws Exception {
		decisionOutcome = new DecisionOutcome();

		if (ECustomerSegment.NON_PRIME.equals(customerSegment)) {
            var consumerSummary = getOrFindConsumerSummary(craRequest).getData();
			BigDecimal incomeExpenditure = calculateIncomeAndExpenditure(consumerSummary, agreementDetail);
			setIncomeExpenditure(incomeExpenditure);

			decisionOutcome.status = incomeExpenditureAssessment.checkOnScheme(incomeExpenditure);

            BigDecimal effectiveDisposableSurplus = effectiveDisposableSurplus(consumerSummary);
			setEffectiveDisposableSurplus(effectiveDisposableSurplus);
		} else if (ECustomerSegment.PRIME.equals(customerSegment)) {
			decisionOutcome.lowInstalment = findLowInstalment(agreementDetail);
			decisionOutcome.status = customerInstalmentCheck(craRequest, agreementDetail);
		}else{
            decisionOutcome.status = EStatus.ON_SCHEME;
        }

		log.info("Decision outcome for Customer Agreement {} : {}", agreementDetail.getAgrAgreementNumber(), decisionOutcome);

		return decisionOutcome;
	}

    private EStatus customerInstalmentCheck(CraRequest craRequest, AgreementDetail agreementDetail) throws Exception {

        if (agreementDetail.getAgrInstalment() != null) {

            if (agreementDetail.getAgrInstalment().compareTo(INSTALMENT_LIMIT) <= 0) {
                setIncomeExpenditure(null);
                setEffectiveDisposableSurplus(null);
                return EStatus.NOT_ON_SCHEME;
            } else {
                var consumerSummary = getOrFindConsumerSummary(craRequest).getData();
                BigDecimal incomeExpenditure = calculateIncomeAndExpenditure(consumerSummary, agreementDetail);
                setIncomeExpenditure(incomeExpenditure);

                BigDecimal effectiveDisposableSurplus = effectiveDisposableSurplus(consumerSummary);
                setEffectiveDisposableSurplus(effectiveDisposableSurplus);

                return incomeExpenditureAssessment.checkOnScheme(incomeExpenditure);
            }
        }
        return EStatus.NOT_CHECKED;
    }

    private ELowInstalment findLowInstalment(AgreementDetail agreementDetail) {
        var isLowInstalment = agreementDetail.getAgrInstalment() != null
            && agreementDetail.getAgrInstalment().compareTo(INSTALMENT_LIMIT) <= 0;

        return isLowInstalment ? ELowInstalment.YES : ELowInstalment.NO;
    }

    private BigDecimal calculateIncomeAndExpenditure(ConsumerSummaryDelphiDataResponse consumerSummary, AgreementDetail agreementDetail) {
        return incomeExpenditureAssessment.calculate(consumerSummary, agreementDetail.getAgrInstalment());
    }

    private BigDecimal effectiveDisposableSurplus(ConsumerSummaryDelphiDataResponse consumerSummary) {
        return effectiveDisposableSurplus.getEffectiveDisposableSurplus(consumerSummary);
    }

    private void setIncomeExpenditure(BigDecimal incomeExpenditure) {
        if (incomeExpenditure != null) {
            decisionOutcome.incomeExpenditure = incomeExpenditure.toString();
        }
    }

    private void setEffectiveDisposableSurplus(BigDecimal effectiveDisposableSurplus) {
        if (effectiveDisposableSurplus != null) {
            decisionOutcome.effectiveDisposableSurplus = effectiveDisposableSurplus.toString();
        }
    }

    private ConsumerSummaryResponse getOrFindConsumerSummary(CraRequest craRequest) throws Exception {

        if (getConsumerSummary() == null) {
            findConsumerSummary(craRequest);
        }

        return getConsumerSummary();
    }

    private void findConsumerSummary(CraRequest craRequest) throws Exception {
        consumerSummary = craService.getConsumerData(craRequest);
    }

    public ConsumerSummaryResponse getConsumerSummary() {
        return consumerSummary;
    }
}
