package com.affordability.customer.regulation;

import com.affordability.service.datafetcher.IDetermineRegulationService;
import com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.response.DataForDetermineRegulationResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.context.annotation.RequestScope;

@Service
@Slf4j
@RequiredArgsConstructor
@RequestScope
public class CustomerDataForRegulationProvider implements ICustomerDataForRegulationProvider {

    private final IDetermineRegulationService dataFetcherService;

    @Override
    public CustomerDataForDetermineRegulation findData(String agreementNumber) {

        var response = this.dataFetcherService.getDataForDetermineRegulation(agreementNumber);
        return convertResponseToModel(response);
    }

    private CustomerDataForDetermineRegulation convertResponseToModel(DataForDetermineRegulationResponse response) {
        var queryDataFromResponse = response.data.get(0);

        var dataForRegulation = new CustomerDataForDetermineRegulation();

        dataForRegulation.setBusinessLine(queryDataFromResponse.personalOrCorporate);
        dataForRegulation.setTraderTypeByCode(queryDataFromResponse.traderCode);
        dataForRegulation.setPolicyValue(queryDataFromResponse.advance);
        dataForRegulation.setNumberOfPartners(queryDataFromResponse.numberOfPartners);

        return dataForRegulation;
    }
}
