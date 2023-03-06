package com.affordability.service.cra;

import com.affordability.service.cra.request.CraRequest;
import com.affordability.service.cra.response.ConsumerSummaryResponse;
import com.affordability.service.cra.response.DelphiResponse;

public interface ICraService {
    DelphiResponse getDelphiData(CraRequest agreementDetail) throws Exception;
    ConsumerSummaryResponse getConsumerData(CraRequest agreementDetail) throws Exception;
}
