package com.affordability.service.datafetcher.affordabilitycustomerdataforregulation.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Getter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@ToString
@JsonIgnoreProperties(ignoreUnknown = true)
public class DataForDetermineRegulationResponse {
    public Integer status;
    public String name;
    public String versionCode;
    public String versionApi;
    public String hostname;
    public String path;
    public String uuidInstance;
    public Integer idRequest;
    public LocalDateTime dateTimeRequest;
    public String versionCore;
    public List<DataForDetermineRegulationQueryResponse> data;
}
