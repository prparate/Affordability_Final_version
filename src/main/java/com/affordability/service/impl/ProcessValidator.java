package com.affordability.service.impl;

import com.affordability.exception.AgreementRecentlyVerifiedException;
import com.affordability.model.ProcessStarted;
import com.affordability.persistence.IProcessStartedRepository;
import com.affordability.service.process.IProcessValidator;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProcessValidator implements IProcessValidator {

    private final IProcessStartedRepository processStartedRepository;

    @Value("${request.time-process-limit-in-seconds}")
    public long TIME_PROCESS_LIMIT_IN_SECONDS;

    private long SKIP_VALIDATION_CONTROL_FLAG = -1;

    @Override
    public synchronized void checkVerificationProcessStarted(String agreementNumber) throws AgreementRecentlyVerifiedException {

        if (shouldProcessVerification()) {
            this.removeIrrelevantAgreementsProcessedAfterTimeLimit();
            this.validateVerificationHasNotBeenRecentlyTriggeredForAgreement(agreementNumber);
        }else {
            logVerificationHasBeenSkipped();
        }
    }

    private void logVerificationHasBeenSkipped() {
        log.warn("checkVerificationProcessStarted has been skipped as the TIME_PROCESS_LIMIT_IN_SECONDS value has been set to {}"
        , SKIP_VALIDATION_CONTROL_FLAG);
    }

    private boolean shouldProcessVerification() {
        return TIME_PROCESS_LIMIT_IN_SECONDS != SKIP_VALIDATION_CONTROL_FLAG;
    }

    private void validateVerificationHasNotBeenRecentlyTriggeredForAgreement(String agreementNumber) throws AgreementRecentlyVerifiedException {
        var result = this.processStartedRepository.findRecordOrAddNewOne(agreementNumber, Instant.now());
        var record = result.getRecord();

        if (result.isAnExistingRecord()) {
            validateProcessTimeForExistingRecord(record);
        }
    }

    private void validateProcessTimeForExistingRecord(ProcessStarted record) throws AgreementRecentlyVerifiedException {
        var agreementNumber = record.getAgreementNumber();
        var processTime = record.getStartedOn();

        log.warn("Agreement '{}' was processed before at {}. Request could be prevented if that happened in less than {} seconds."
            , agreementNumber
            , processTime
            , this.TIME_PROCESS_LIMIT_IN_SECONDS);

        var agreementRecentlyProcessed = this.hasBeenRecentlyProcessed(processTime);

        if (agreementRecentlyProcessed) {
            throwRecentlyVerifiedException(agreementNumber, processTime);
        }
    }

    private void throwRecentlyVerifiedException(String agreementNumber, Instant processTime) throws AgreementRecentlyVerifiedException {
        log.error("Process will be stop and exception will be triggered for agreement '{}' which has been processed at {} (in the last {} seconds)."
            , agreementNumber
            , processTime
            , this.TIME_PROCESS_LIMIT_IN_SECONDS);

        throw new AgreementRecentlyVerifiedException(agreementNumber, this.TIME_PROCESS_LIMIT_IN_SECONDS);
    }

    private void removeIrrelevantAgreementsProcessedAfterTimeLimit() {
        this.processStartedRepository.lockAndRemoveRecordsIfStartedTimeIsGreaterThan(this.TIME_PROCESS_LIMIT_IN_SECONDS);
    }

    private boolean hasBeenRecentlyProcessed(Instant time) {
        var now = Instant.now();
        var duration = Duration.between(time, now);
        var durationInSeconds = duration.getSeconds();

        var outOfLimit = durationInSeconds <= this.TIME_PROCESS_LIMIT_IN_SECONDS;

        if (outOfLimit) {
           log.warn("Calculated time is outOfLimit {} seconds, limit is {} seconds"
               , durationInSeconds
               , this.TIME_PROCESS_LIMIT_IN_SECONDS);
        } else {
            log.info("Calculated time is {} seconds, greater than {} seconds which is the current limit"
                , durationInSeconds
                , this.TIME_PROCESS_LIMIT_IN_SECONDS);
        }

        return outOfLimit;
    }
}
