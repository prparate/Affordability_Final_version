package com.affordability.scheduler;

import com.affordability.customer.segmentation.ECustomerSegment;
import com.affordability.model.*;
import com.affordability.persistence.IAffordabilityOutcomeRepository;
import com.affordability.persistence.IErrorCraUpdateRepository;
import com.affordability.persistence.jpa.AffordabilityOutcomeJPA;
import com.affordability.persistence.jpa.ErrorCraUpdate;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestTemplate;

import java.util.Collections;
import java.util.Optional;
import java.util.UUID;

import static com.affordability.model.EBatchStatus.PENDING;
import static com.affordability.utils.EnumUtil.getEnumName;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.*;

@SpringBootTest
class ErrorCraUpdateJobTest {

    @Autowired
    private ErrorCraUpdateJob job;

    @MockBean
    private RestTemplate restTemplate;

    @MockBean
    private IAffordabilityOutcomeRepository affordabilityOutcomeRepository;

    @MockBean
    private IErrorCraUpdateRepository errorCraUpdateRepository;


    @Test
    @DisplayName("When get empty list of errors should not throw exception")
    void whenGetEmptyListOfErrorsShouldNotThrowException() {
        assertDoesNotThrow(() -> job.execute());
    }

    @Test
    @DisplayName("when get processed error should not process")
    void whenGetProcessedErrorShouldNotProcess() {
        var outcomeId = UUID.randomUUID();

        when(errorCraUpdateRepository.findOutcomeIdByBatchStatus(getEnumName(PENDING)))
                .thenReturn(Collections.singletonList(outcomeId));

        var domainModel = new AffordabilitySummary("00000001", EActivityType.NEW, "test");
        when(affordabilityOutcomeRepository.findById(any()))
                .thenReturn(Optional.of(AffordabilityOutcomeJPA.builder()
                        .affordabilitySummaryJsonData(domainModel)
                        .build()));


        assertDoesNotThrow(() -> job.execute());
    }

    @Test
    @DisplayName("when get error and is allow should process")
    void whenGetErrorAndIsAllowShouldProcess() {
        var outcomeId = UUID.randomUUID();

        when(errorCraUpdateRepository.findOutcomeIdByBatchStatus(getEnumName(PENDING)))
                .thenReturn(Collections.singletonList(outcomeId));

        var domainModel = new AffordabilitySummary("00000001", EActivityType.NEW, "test");

        domainModel.automatedDecision = EAutomatedDecision.PASS;
        domainModel.isFunded = true;
        domainModel.customerSegment = ECustomerSegment.PRIME;
        domainModel.status = EStatus.ON_SCHEME;

        when(affordabilityOutcomeRepository.findById(any()))
                .thenReturn(Optional.of(AffordabilityOutcomeJPA.builder()
                        .affordabilitySummaryJsonData(domainModel)
                        .build()));

        when(errorCraUpdateRepository.findByOutcomeId(any()))
                .thenReturn(Optional.of(ErrorCraUpdate.builder()
                        .outcomeId(outcomeId)
                        .status(EBatchStatus.PENDING.toString())
                        .build()));

        when(restTemplate.postForObject(anyString(), any(), any()))
                .thenReturn("{\"status\":\"success\"}");

        assertDoesNotThrow(() -> job.execute());
    }

}
