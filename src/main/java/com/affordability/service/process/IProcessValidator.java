package com.affordability.service.process;

import com.affordability.exception.AgreementRecentlyVerifiedException;

public interface IProcessValidator {

    void checkVerificationProcessStarted(String agreementNumber) throws AgreementRecentlyVerifiedException;
}
