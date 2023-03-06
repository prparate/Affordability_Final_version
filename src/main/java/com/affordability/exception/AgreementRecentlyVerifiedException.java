package com.affordability.exception;

public class AgreementRecentlyVerifiedException extends Exception {
    private final String agreementNumber;
    private final long secondsOfControl;

    public AgreementRecentlyVerifiedException(String agreementNumber, long secondsOfControl) {
        this.agreementNumber = agreementNumber;
        this.secondsOfControl = secondsOfControl;
    }

    @Override
    public String getMessage() {
        return String.format("The agreement number '%s' has been processed in the last %d seconds. Try again after this period of time."
            , agreementNumber, secondsOfControl);
    }
}
