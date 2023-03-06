package com.affordability.thread;

import com.affordability.model.AffordabilitySummary;
import com.affordability.model.EBatchStatus;
import com.affordability.persistence.IErrorCraUpdateRepository;
import com.affordability.persistence.jpa.ErrorCraUpdate;
import com.affordability.utils.EnumUtil;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestTemplate;

import java.time.Instant;
import java.util.UUID;

import static com.affordability.model.EBatchStatus.PENDING;
import static com.affordability.utils.EnumUtil.getEnumName;

@Slf4j
public class UpdateCraThread extends Thread {

    private final RetryTemplate retryTemplate;
    private final RestTemplate restTemplate;
    private final UUID outcomeId;
    private final AffordabilitySummary affordabilitySummary;
    private final IErrorCraUpdateRepository errorCraUpdateRepository;
    private final String updateCraUrl;

    public UpdateCraThread(RetryTemplate retryTemplate, RestTemplate restTemplate, UUID outcomeId,
                           AffordabilitySummary affordabilitySummary, IErrorCraUpdateRepository
                                   errorCraUpdateRepository, String updateCraUrl) {
        super("cra_" + affordabilitySummary.agreementNumber);
        this.retryTemplate = retryTemplate;
        this.restTemplate = restTemplate;
        this.outcomeId = outcomeId;
        this.affordabilitySummary = affordabilitySummary;
        this.errorCraUpdateRepository = errorCraUpdateRepository;
        this.updateCraUrl = updateCraUrl;
    }

    @Override
    public void run() {

        log.info("Agreement: %s update CRA".formatted(affordabilitySummary.agreementNumber));
        retryTemplate.execute(
                value -> {
                    restTemplate.postForObject(updateCraUrl, affordabilitySummary.getBodyToCraUpdate(), String.class);
                    log.info("Agreement {} updated at CRA", affordabilitySummary.agreementNumber);
                    return true;
                },
                value -> {
                    var throwable = value.getLastThrowable();
                    log.error("Agreement {} could not be updated at CRA", affordabilitySummary.agreementNumber);
                    log.error(StringUtils.EMPTY, throwable);

                    errorCraUpdateRepository.save(ErrorCraUpdate.builder()
                            .createdAt(Instant.now())
                            .outcomeId(outcomeId)
                            .errorMessage(throwable.getMessage())
                            .status(getEnumName(PENDING))
                            .build());

                    return false;
                }
        );

    }
}
