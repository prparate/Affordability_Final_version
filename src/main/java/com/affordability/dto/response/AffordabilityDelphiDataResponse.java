package com.affordability.dto.response;

import com.affordability.service.cra.response.DataSegmentResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class AffordabilityDelphiDataResponse implements Serializable {

	@JsonProperty("NDSPCII")
	private DataSegmentResponse ndspcii;

	@JsonProperty("E1B07")
	private DataSegmentResponse e1b07;

	@JsonProperty("E1B08")
	private DataSegmentResponse e1b08;

	@JsonProperty("EA1C01")
	private DataSegmentResponse ea1c01;

	@JsonProperty("EA4Q05")
	private DataSegmentResponse ea4q05;

	@JsonProperty("E1A07")
	private DataSegmentResponse e1a07;

	@JsonProperty("E1D02")
	private DataSegmentResponse e1d02;

	@JsonProperty("E1B09")
	private DataSegmentResponse e1b09;

	@JsonProperty("NDECC03")
	private DataSegmentResponse ndecc03;

	@JsonProperty("error_message")
	private DataSegmentResponse errorMessage;

    private String uuid;
}
