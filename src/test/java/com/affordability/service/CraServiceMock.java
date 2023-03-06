package com.affordability.service;

import com.affordability.service.cra.ICraService;
import com.affordability.service.cra.request.CraRequest;
import com.affordability.service.cra.response.ConsumerSummaryResponse;
import com.affordability.service.cra.response.DelphiResponse;

public class CraServiceMock implements ICraService {
    public DelphiResponse getDelphiDataReturn = new DelphiResponse();
    public ConsumerSummaryResponse getConsumerDataReturn = new ConsumerSummaryResponse();

    @Override
    public DelphiResponse getDelphiData(CraRequest agreementDetail) throws Exception {
        return getDelphiDataReturn;
    }

    @Override
    public ConsumerSummaryResponse getConsumerData(CraRequest agreementDetail) throws Exception {
        return getConsumerDataReturn;
    }
}
