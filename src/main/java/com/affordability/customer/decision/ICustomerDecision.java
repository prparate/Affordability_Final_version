package com.affordability.customer.decision;

import com.affordability.customer.segmentation.ECustomerSegment;
import com.affordability.dto.response.AffordabilityDelphiDataResponse;
import com.affordability.dto.response.AgreementDetail;
import com.affordability.dto.response.ConsumerSummaryDelphiDataResponse;
import com.affordability.service.cra.request.CraRequest;
import com.affordability.service.cra.response.ConsumerSummaryResponse;

public interface ICustomerDecision {

    DecisionOutcome processCustomerSegment(ECustomerSegment customerSegment, AffordabilityDelphiDataResponse delphiDataResponse, CraRequest craRequest, AgreementDetail agreementDetails) throws Exception;
    ConsumerSummaryResponse getConsumerSummary();
}
