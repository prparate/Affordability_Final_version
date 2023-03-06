package com.affordability.controller;

import com.affordability.dto.request.AffordabilityVerification;
import com.affordability.dto.response.DataFetcherResponse;
import com.affordability.service.cra.response.ConsumerSummaryResponse;
import com.affordability.service.cra.response.DelphiResponse;
import com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.response.DataForDetermineRegulationResponse;
import com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response.PartnerDetailsForCommercialAgreementResponse;
import org.springframework.test.web.servlet.ResultMatcher;
import org.springframework.web.client.RestClientException;

import java.util.Collection;

public record TestControllerParam(String name,
                                  DataFetcherResponse dataFetcherResponse,
                                  DataForDetermineRegulationResponse dataForDetermineRegulationResponse,
                                  PartnerDetailsForCommercialAgreementResponse partnerDetailsForCommercialAgreementResponse,
                                  DelphiResponse delphiResponse,
                                  ConsumerSummaryResponse consumerSummaryResponse,
                                  AffordabilityVerification reqBody,
                                  Collection<ResultMatcher> resultsMatcher,
                                  RestClientException delphiResponseEx,
                                  RestClientException consumerSummaryResponseEx,

                                  RestClientException updateCraResponseException
) {
    @Override
    public String toString() {
        return name;
    }
}