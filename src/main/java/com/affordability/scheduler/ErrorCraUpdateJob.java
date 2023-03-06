package com.affordability.scheduler;

import com.affordability.model.AffordabilitySummary;
import com.affordability.model.EBatchStatus;
import com.affordability.persistence.IAffordabilityOutcomeRepository;
import com.affordability.persistence.IErrorCraUpdateRepository;
import com.affordability.persistence.jpa.AffordabilityOutcomeJPA;
import com.affordability.persistence.jpa.ErrorCraUpdate;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

import java.util.Optional;
import java.util.UUID;

import static com.affordability.model.EBatchStatus.PENDING;
import static com.affordability.model.EBatchStatus.getEnumByString;
import static com.affordability.utils.EnumUtil.getEnumName;

@Component
@RequiredArgsConstructor
@Slf4j
public class ErrorCraUpdateJob {
    private final IErrorCraUpdateRepository errorCraUpdateRepository;

    private final IAffordabilityOutcomeRepository affordabilityOutcomeRepository;

    @Value("${cra.base-url}/v1/client-agreement/affordability-schema")
    private String updateCraUrl;

    private final RestTemplate restTemplate;

    //DEFAULT SCHEDULE = EVERY DAY AT MIDNIGHT
    @Scheduled(cron = "${cra.cron.error-update:0 0 0 * * *}")
    public void execute() {
        log.info("===============Started ErrorCraUpdateJob====================");

        var outcomesId = errorCraUpdateRepository.findOutcomeIdByBatchStatus(getEnumName(PENDING));

        for(var outcomeId : outcomesId){
            executeById(outcomeId);
        }

        log.info("===============Completed ErrorCraUpdateJob===================");
    }

    private void executeById(UUID outcomeId) {
        var optionalErrorCraUpdate = errorCraUpdateRepository.findByOutcomeId(outcomeId);
        var optionalOutcome = affordabilityOutcomeRepository.findById(outcomeId);

        if (optionalErrorCraUpdate.isEmpty() || optionalOutcome.isEmpty()
                || PENDING != getEnumByString(optionalErrorCraUpdate.get().getStatus())
                || !optionalOutcome.get().getAffordabilitySummaryJsonData().isAllowedToUpdateCra()) {

            log.info("OutcomeId: {} is not eligible for ErrorCraUpdateJob", outcomeId);
            optionalErrorCraUpdate.ifPresent(this::markAsFailed);
            return;
        }

        var errorCraUpdate = optionalErrorCraUpdate.get();
        markAsProcessing(errorCraUpdate);

        var affordabilitySummary = optionalOutcome.get().getAffordabilitySummaryJsonData();

        executeUpdateRequestAndUpdateStatus(outcomeId, errorCraUpdate, affordabilitySummary);
    }

    private void executeUpdateRequestAndUpdateStatus(UUID outcomeId, ErrorCraUpdate errorCraUpdate, AffordabilitySummary affordabilitySummary) {
        try {
            restTemplate.postForObject(updateCraUrl, affordabilitySummary.getBodyToCraUpdate(), String.class);
            markAsCompleted(errorCraUpdate);
            log.info("Agreement {} updated at CRA", affordabilitySummary.agreementNumber);
        } catch (Exception e) {
            log.error("Error while updating CRA for OutcomeId: {}", outcomeId);
            log.error(StringUtils.EMPTY, e);
            markAsFailed(errorCraUpdate);
        }
    }

    private void markAsProcessing(ErrorCraUpdate errorCraUpdate) {
        extracted(errorCraUpdate, EBatchStatus.PROCESSING);
    }

    private void markAsCompleted(ErrorCraUpdate errorCraUpdate) {
        extracted(errorCraUpdate, EBatchStatus.COMPLETED);
    }

    private void markAsFailed(ErrorCraUpdate errorCraUpdate) {
        extracted(errorCraUpdate, EBatchStatus.FAILED);
    }

    private void extracted(ErrorCraUpdate errorCraUpdate, EBatchStatus status) {
        errorCraUpdate.setStatus(getEnumName(status));
        errorCraUpdateRepository.save(errorCraUpdate);
    }

}
