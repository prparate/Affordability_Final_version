package com.affordability.service.datafetcher;

import com.affordability.dto.response.AgreementDetail;

public interface IAgreementSummaryService {
    AgreementDetail getAgreementSummary(String agreementNumber);
}
