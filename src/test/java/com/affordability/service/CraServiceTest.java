package com.affordability.service;

import com.affordability.exception.MicroservicesConnectionException;
import com.affordability.service.cra.request.CraRequest;
import com.affordability.service.cra.response.ConsumerSummaryResponse;
import com.affordability.service.cra.response.DelphiResponse;
import com.affordability.service.cra.ICraService;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.when;


@SpringBootTest
class CraServiceTest {

	@Autowired
	private ICraService craService;

	@MockBean
	@Qualifier("CraDelphi")
	private RestTemplate restTemplate;


	static Stream<CraRequest> agreementDetailProvider(){
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
		return Stream.of(detail);
	}

	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should return Success when all parameters are present and CRA service is up and running")
	void shouldReturnSuccessWhenAllParametersArePresent(CraRequest agreementDetail) throws Exception{

		var craResponse = readJson("mock/cra/credit-check/cra-credit-check-success-response.json");

		when(restTemplate.getForObject(anyString(), any()))
				.thenReturn(craResponse);

		var response = craService.getDelphiData(agreementDetail);

		assertEquals(response,craResponse);
	}

	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should throw RestClient Exception when CRA service is not running")
	void shouldThrowRestClientExceptionWhenCRAisNotRunning(CraRequest agreementDetail) throws Exception{

		when(restTemplate.getForObject(anyString(), any()))
				.thenThrow(new RestClientException("CRA connection and retry timeout"));

		assertThrows(MicroservicesConnectionException.class,
				() -> craService.getDelphiData(agreementDetail));

	}

	@Test
	@DisplayName("Should throw Exception when any of the parameter is missing")
	void shouldThrowExceptionWhenAnyParameterIsMissing() throws Exception{

		assertThrows(new Exception().getClass(),
				() -> craService.getDelphiData(new CraRequest()));
	}

	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should throw Exception when response data object is empty")
	void shouldThrowExceptionWhenResponseDataObjectIsEmpty(CraRequest agreementDetail) throws Exception {

		var craResponse = readJson("mock/cra/credit-check/cra-credit-check-no-response.json");

		when(restTemplate.getForObject(anyString(), any()))
				.thenReturn(craResponse);

		assertThrows(Exception.class,
				() -> craService.getDelphiData(agreementDetail));
	}

	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should throw Exception when response data object is absent")
	void shouldThrowExceptionWhenResponseDataObjectIsAbsent(CraRequest agreementDetail) throws Exception {

		var craResponse = readJson("mock/cra/credit-check/cra-credit-check-no-data-response.json");

		when(restTemplate.getForObject(anyString(), any()))
				.thenReturn(craResponse);

		assertThrows(Exception.class,
				() -> craService.getDelphiData(agreementDetail));
	}

	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should throw Exception when data object contains error")
	void shouldThrowExceptionWhenDataContainsError(CraRequest agreementDetail) throws Exception {

		var craResponse = readJson("mock/cra/credit-check/cra-credit-check-error-response.json");

		when(restTemplate.getForObject(anyString(), any()))
				.thenReturn(craResponse);

		assertThrows(Exception.class,
				() -> craService.getDelphiData(agreementDetail));
	}

	private static DelphiResponse readJson(String fileName) throws IOException {
		var inputStream = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(fileName);
		var map = new ObjectMapper();

		return map.readValue(inputStream, DelphiResponse.class);

	}


	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should return Success when all parameters are present and CRA service is up and running")
	void getConsumerDataShouldReturnSuccessWhenAllParametersArePresent(CraRequest agreementDetail) throws Exception{

		var craResponse = readAffordabilityJson("mock/cra/affordability-check/cra-affordability-check-success-response.json");
		when(restTemplate.getForObject(anyString(), any()))
				.thenReturn(craResponse);

		var response = craService.getConsumerData(agreementDetail);

		assertEquals(response,craResponse);
	}

	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should throw RestClient Exception when CRA service is not running")
	void getConsumerDataShouldThrowRestClientExceptionWhenCRAisNotRunning(CraRequest agreementDetail) throws Exception{

		when(restTemplate.getForObject(anyString(), any()))
				.thenThrow(new RestClientException("CRA connection and retry timeout"));

		assertThrows(MicroservicesConnectionException.class,
				() -> craService.getConsumerData(agreementDetail));

	}

	@Test
	@DisplayName("Should throw Exception when any of the parameter is missing")
	void getConsumerDataShouldThrowExceptionWhenAnyParameterIsMissing() throws Exception{

		assertThrows(new Exception().getClass(),
				() -> craService.getConsumerData(new CraRequest()));
	}

	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should throw Exception when response data object is empty")
	void getConsumerDataShouldThrowExceptionWhenResponseDataObjectIsEmpty(CraRequest agreementDetail) throws Exception {

		var craResponse = readAffordabilityJson("mock/cra/affordability-check/cra-affordability-check-no-response.json");

		when(restTemplate.getForObject(anyString(), any()))
				.thenReturn(craResponse);

		assertThrows(Exception.class,
				() -> craService.getConsumerData(agreementDetail));
	}

	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should throw Exception when response data object is absent")
	void getConsumerDataShouldThrowExceptionWhenResponseDataObjectIsAbsent(CraRequest agreementDetail) throws Exception {

		var craResponse = readAffordabilityJson("mock/cra/affordability-check/cra-affordability-check-no-data-response.json");

		when(restTemplate.getForObject(anyString(), any()))
				.thenReturn(craResponse);

		assertThrows(Exception.class,
				() -> craService.getConsumerData(agreementDetail));
	}

	@ParameterizedTest
	@MethodSource("agreementDetailProvider")
	@DisplayName("Should throw Exception when data object contains error")
	void getConsumerDataShouldThrowExceptionWhenDataContainsError(CraRequest agreementDetail) throws Exception {

		var craResponse = readAffordabilityJson("mock/cra/affordability-check/cra-affordability-check-error-response.json");

		when(restTemplate.getForObject(anyString(), any()))
				.thenReturn(craResponse);

		assertThrows(Exception.class,
				() -> craService.getConsumerData(agreementDetail));
	}

	private static ConsumerSummaryResponse readAffordabilityJson(String fileName) throws IOException {
		var inputStream = Thread.currentThread()
				.getContextClassLoader()
				.getResourceAsStream(fileName);
		var map = new ObjectMapper();

		return map.readValue(inputStream, ConsumerSummaryResponse.class);

	}

}
