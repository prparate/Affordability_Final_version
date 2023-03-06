package com.affordability.model;

public class FindProcessStartedResult {
    private ProcessStarted record;
    private boolean isAnExistingRecord;

    public ProcessStarted getRecord() {
        return record;
    }

    public void setRecord(ProcessStarted record) {
        this.record = record;
    }

    public boolean isAnExistingRecord() {
        return isAnExistingRecord;
    }

    public void setIsAnExistingRecord(boolean isAnExistingRecord) {
        this.isAnExistingRecord = isAnExistingRecord;
    }
}
