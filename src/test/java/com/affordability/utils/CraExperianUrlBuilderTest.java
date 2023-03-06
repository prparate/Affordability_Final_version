package com.affordability.utils;

import com.affordability.dto.response.AgreementDetail;
import com.affordability.service.cra.request.CraRequest;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.math.BigDecimal;
import java.nio.charset.StandardCharsets;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;

@SpringBootTest
class CraExperianUrlBuilderTest {

	CraExperianUrlBuilder craExperianUrlBuilder = new CraExperianUrlBuilder();

	static Stream<CraRequest> agreementPersonalDetailProvider(){
		CraRequest detail =  new CraRequest();
		detail.setPersonDateOfBirth("1958-09-10 00:00:00");
		detail.setPersonTitle("person_title");
		detail.setPersonNameFirst("person_name_first");
		detail.setPersonNameLast("person_name_last");
		detail.setBankAccountNumber("bank_account_number");
		detail.setBankAccountSortCode("bank_account_sort_code");
		detail.setAgrAdvance(new BigDecimal(1));
		detail.setPersonHouseNumber("person_house_number");
		detail.setPersonStreetName("person_street_name");
		detail.setPersonLocality("person_locality");
		detail.setPersonPostCode("person_post_code");
		detail.setPersonPostTown("person_post_town");
		detail.setPersonCountyCode("person_county_code");
		detail.setPersonCountry("person_country");
		detail.setHabitationDateIn("1958-09-10 00:00:00");
		detail.setHabitationDateOut("1978-09-10 00:00:00");
		return Stream.of(detail);
	}

	@ParameterizedTest
	@MethodSource("agreementPersonalDetailProvider")
	@DisplayName("Should return Success when CRA-credit check Personal url matches with all parameters")
	void shouldReturnSuccessWhenPersonalUrlMatchesWithAllParameters(CraRequest agreementDetail) throws Exception{

		var expectedResponse = readText("mock/cra/credit-check/cra-credit-check-personal-url.txt");
		var actualResponse = craExperianUrlBuilder.buildCraExperianUrl(agreementDetail, "http://10.6.0.5:8027","v1","credit/check");

		assertEquals(expectedResponse,actualResponse);
	}

	private static String readText(String fileName) throws IOException {
		var inputStream = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(fileName);

		String expectedResult = new String(inputStream.readAllBytes(), StandardCharsets.UTF_8);

		return expectedResult;

	}
}
