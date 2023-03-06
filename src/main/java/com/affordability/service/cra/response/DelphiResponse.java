package com.affordability.service.cra.response;

import com.affordability.dto.response.AffordabilityDelphiDataResponse;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@Getter
@Setter
@ToString
@NoArgsConstructor
@AllArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
public class DelphiResponse {

	private String uuidInstance;

	private String datetimeRequest;

	private AffordabilityDelphiDataResponse data;

}
