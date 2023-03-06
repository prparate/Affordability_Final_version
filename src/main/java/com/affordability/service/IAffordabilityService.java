package com.affordability.service;

import com.affordability.dto.request.AffordabilityVerification;
import com.affordability.dto.response.AgreementLastRecordResponse;
import com.affordability.exception.ApplicationException;
import com.affordability.model.AffordabilitySummary;

public interface IAffordabilityService {

    AffordabilitySummary verify(AffordabilityVerification affordabilityVerification) throws Exception;

    AgreementLastRecordResponse getLastOutcome(String agreementNumber, String environment) throws ApplicationException;
}
