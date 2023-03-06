package com.affordability.persistence;

import com.affordability.model.FindProcessStartedResult;

import java.time.Instant;

public interface IProcessStartedRepository {
    void lockAndRemoveRecordsIfStartedTimeIsGreaterThan(long limitInSeconds);

    FindProcessStartedResult findRecordOrAddNewOne(String agreementNumber, Instant now);
}
