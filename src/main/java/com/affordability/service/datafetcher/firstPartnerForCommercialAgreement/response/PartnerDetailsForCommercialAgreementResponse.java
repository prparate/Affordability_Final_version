package com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;
import java.util.List;

@NoArgsConstructor
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@Setter
@ToString
public class PartnerDetailsForCommercialAgreementResponse {

	private Integer status;
	private String name;
	private String versionCode;
	private String versionApi;
	private String hostname;
	private String path;
	private String uuidInstance;
	private Integer idRequest;
	private LocalDateTime dateTimeRequest;
	private String versionCore;
	private List<PartnerDetailsForCommercialAgreementQueryResponse> data;
}
