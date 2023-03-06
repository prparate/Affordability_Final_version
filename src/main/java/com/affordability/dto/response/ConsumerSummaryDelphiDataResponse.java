package com.affordability.dto.response;

import com.affordability.service.cra.response.DataSegmentResponse;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonIgnoreProperties(ignoreUnknown = true)
public class ConsumerSummaryDelphiDataResponse implements Serializable {

	@JsonProperty("SPEDI03")
	private DataSegmentResponse spedi03;

	@JsonProperty("SPEDI04")
	private DataSegmentResponse spedi04;

	@JsonProperty("SPEDI05")
	private DataSegmentResponse spedi05;

	@JsonProperty("SPEDI06")
	private DataSegmentResponse spedi06;

	@JsonProperty("SPEDI08")
	private DataSegmentResponse spedi08;

	@JsonProperty("error_message")
	private DataSegmentResponse errorMessage;

    private String uuid;

	@JsonIgnore
	public boolean hasNullNegativeOrInvalidValue(){
		if (getSpedi03() == null || getSpedi03().getValueAsBigDecimal() == null
				|| getSpedi03().getValueAsBigDecimal().compareTo(BigDecimal.ZERO) < 0)
			return true;
		if (getSpedi04() == null || getSpedi04().getValueAsBigDecimal() == null
				|| getSpedi04().getValueAsBigDecimal().compareTo(BigDecimal.ZERO) < 0)
			return true;
		if (getSpedi05() == null || getSpedi05().getValueAsBigDecimal() == null
				|| getSpedi05().getValueAsBigDecimal().compareTo(BigDecimal.ZERO) < 0)
			return true;
		if (getSpedi08() == null || getSpedi08().getValueAsBigDecimal() == null
				|| getSpedi08().getValueAsBigDecimal().compareTo(BigDecimal.ZERO) < 0)
			return true;
		return false;
	}
}
