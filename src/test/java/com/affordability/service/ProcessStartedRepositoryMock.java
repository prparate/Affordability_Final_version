package com.affordability.service;

import com.affordability.model.FindProcessStartedResult;
import com.affordability.model.ProcessStarted;
import com.affordability.persistence.IProcessStartedRepository;

import java.time.Instant;

public class ProcessStartedRepositoryMock implements IProcessStartedRepository {

    private FindProcessStartedResult findRecordOrAddNewOneReturn;

    @Override
    public void lockAndRemoveRecordsIfStartedTimeIsGreaterThan(long limitInSeconds) {

    }

    @Override
    public FindProcessStartedResult findRecordOrAddNewOne(String agreementNumber, Instant now) {
        return this.findRecordOrAddNewOneReturn;
    }

    public void setupRecordToBeReturnedInFindRecordOrAddNewOneFunction(String agreementNumber, Instant startedOn, boolean isExistingRecord) {

        var processStarted = new ProcessStarted();
        processStarted.setAgreementNumber(agreementNumber);
        processStarted.setStartedOn(startedOn);

        var result = new FindProcessStartedResult();
        result.setRecord(processStarted);
        result.setIsAnExistingRecord(isExistingRecord);

        this.findRecordOrAddNewOneReturn = result;
    }
}
