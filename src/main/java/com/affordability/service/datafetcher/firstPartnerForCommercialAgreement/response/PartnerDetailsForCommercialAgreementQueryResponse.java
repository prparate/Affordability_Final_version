package com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.response;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.PropertyNamingStrategies;
import com.fasterxml.jackson.databind.annotation.JsonNaming;
import lombok.*;

@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonNaming(PropertyNamingStrategies.SnakeCaseStrategy.class)
@JsonIgnoreProperties(ignoreUnknown = true)
@Getter
@ToString
public class PartnerDetailsForCommercialAgreementQueryResponse {

	private String agreementNumber;
	private String personTitle;
	private String personForenames;
	private String personMiddleName;
	private String personSurname;
	private String styled;
	private String dateOfBirth;
	private String emailAddress;
	private String houseName;
	private String houseNumber;
	private String streetName;
	private String streetType;
	private String postTown;
	private String locality;
	private String postalCode;
	private String countyCode;
	private String country;
	private String habitationDateIn;
	private String habitationDateOut;

}
