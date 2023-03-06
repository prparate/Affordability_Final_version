package com.affordability.service.datafetcher;

import com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.response.DataForDetermineRegulationResponse;

public interface IDetermineRegulationService {
    DataForDetermineRegulationResponse getDataForDetermineRegulation(String agreementNumber);
}
