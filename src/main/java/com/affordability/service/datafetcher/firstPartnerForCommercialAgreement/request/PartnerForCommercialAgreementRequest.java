package com.affordability.service.datafetcher.firstPartnerForCommercialAgreement.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Getter
@ToString
@AllArgsConstructor
@Builder
public class PartnerForCommercialAgreementRequest {
	private PartnerForCommercialAgreementArgumentRequest arguments;
}
