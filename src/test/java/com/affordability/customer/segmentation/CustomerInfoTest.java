package com.affordability.customer.segmentation;


import com.affordability.dto.response.AffordabilityDelphiDataResponse;

import com.affordability.service.cra.response.DataSegmentResponse;
import org.springframework.boot.test.context.SpringBootTest;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;


@SpringBootTest
class CustomerInfoTest {

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForSuccess")
    @DisplayName("Should return true when When Enhanced Customer Indebtedness Index : 0 <= G4_ND_SPCII < 40")
    void enhancedCustomerIndebtednessIndexForPrime(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertTrue(customer.enhancedCustomerIndebtednessIndexForPrime(), "Check value of NDSPCII at index '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForFailure")
    @DisplayName("Should return false when When Enhanced Customer Indebtedness Index :  G4_ND_SPCII >= 40 or G4_ND_SPCII < 0")
    void enhancedCustomerIndebtednessIndexForPrimeFailure(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertFalse(customer.enhancedCustomerIndebtednessIndexForPrime(), "Check value of NDSPCII at '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }


    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForSuccess")
    @DisplayName("Should return true when When No payment arrears in the last 6 months : CAIS Status E1_B_07 IN ('0', ‘1', ‘N’, 'U’)")
    void worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertTrue(customer.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime(), "Check value of E1B07 at index '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForFailure")
    @DisplayName("Should return false when When No payment arrears in the last 6 months :  CAIS Status other than E1_B_07 IN ('0', ‘1', ‘N’, 'U’)")
    void worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrimeFailure(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertFalse(customer.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforPrime(), "Check value of E1B07 at index '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForSuccess")
    @DisplayName("Should return true when No CCJs, no IVA or Bankruptcy in the last 6 years : EA1_C_01 = “N”")
    void detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertTrue(customer.detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime(), "Check value of EA1C01 at index '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForFailure")
    @DisplayName("Should return false when No CCJs, no IVA or Bankruptcy in the last 6 years : EA1_C_01 != “N”")
    void detectedCCJsOrIVAOrBankruptcyInLast6YearforPrimeFailure(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertFalse(customer.detectedCCJsOrIVAOrBankruptcyInLast6YearforPrime(), "Check value of EA1C01 at index '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForSuccess")
    @DisplayName("Should return true Number of Settled Good CAIS Accounts + Active CAIS Accounts greater or equal to 3 : SUM(E1_A_07, E1_D_02, E1_B_09) >= 3")
    void settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertTrue(customer.settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin(), "Check value of E1_A_07, E1_D_02, E1_B_09 at index '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForFailure")
    @DisplayName("Should return false Number of Settled Good CAIS Accounts + Active CAIS Accounts greater or equal to 3 : SUM(E1_A_07, E1_D_02, E1_B_09) < 3")
    void settledGoodCAISAccountsActiveCAISAccountsGreaterThanMineFailure(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertFalse(customer.settledGoodCAISAccountsActiveCAISAccountsGreaterThanMin(), "Check value of E1_A_07, E1_D_02, E1_B_09 at index '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForSuccess")
    @DisplayName("Should return true Number of Active CAIS Accounts is greater than or equal to Active Non-Delinquent CAIS Accounts opened in the last 6 months : E1_B_09 >= NDECC03")
    void numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertTrue(customer.numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts(), "Check value of E1_B_09 & NDECC03 at index '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }

    @ParameterizedTest
    @MethodSource("CreateJsonPropertyMockForFailure")
    @DisplayName("Should return false Number of Active CAIS Accounts is greater than or equal to Active Non-Delinquent CAIS Accounts opened in the last 6 months : E1_B_09 < NDECC03”")
    void numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccountsFailure(List<ICustomerInfo> customerInfoList) {
        var index = 0;
        for (ICustomerInfo customer : customerInfoList) {
            assertFalse(customer.numOfActiveCAISAccGreaterOrEqualToActiveNonDelinquentAccounts(), "Check value of E1_B_09 & NDECC03 at index '" + index + "' of List<ICustomerInfo>");
            index++;
        }
    }

    static Stream<List<ICustomerInfo>> CreateJsonPropertyMockForSuccess() {
        var mocks = new ArrayList<ICustomerInfo>();

        var mock = new CustomerInfo();
        var delBlock = new AffordabilityDelphiDataResponse();

        delBlock.setNdspcii(new DataSegmentResponse("NDSPCII", "0"));
        delBlock.setE1b07(new DataSegmentResponse("E1B07", "0"));
        delBlock.setEa1c01(new DataSegmentResponse("EA1C01", "N"));
        delBlock.setE1a07(new DataSegmentResponse("E1A07", "1"));
        delBlock.setE1d02(new DataSegmentResponse("E1D02", "1"));
        delBlock.setE1b09(new DataSegmentResponse("E1B09", "1"));
        delBlock.setNdecc03(new DataSegmentResponse("NDECC03", "0"));
        mock.setDelphiBlock(delBlock);
        mocks.add(mock);


        mock = new CustomerInfo();
        delBlock = new AffordabilityDelphiDataResponse();

        delBlock.setNdspcii(new DataSegmentResponse("NDSPCII", "1"));
        delBlock.setE1b07(new DataSegmentResponse("E1B07", "1"));
        delBlock.setEa1c01(new DataSegmentResponse("EA1C01", "N"));
        delBlock.setE1a07(new DataSegmentResponse("E1A07", "1"));
        delBlock.setE1d02(new DataSegmentResponse("E1D02", "0"));
        delBlock.setE1b09(new DataSegmentResponse("E1B09", "2"));
        delBlock.setNdecc03(new DataSegmentResponse("NDECC03", "0"));
        mock.setDelphiBlock(delBlock);

        mock = new CustomerInfo();
        delBlock = new AffordabilityDelphiDataResponse();

        delBlock.setNdspcii(new DataSegmentResponse("NDSPCII", "30"));
        delBlock.setE1b07(new DataSegmentResponse("E1B07", "N"));
        delBlock.setEa1c01(new DataSegmentResponse("EA1C01", "N"));
        delBlock.setE1a07(new DataSegmentResponse("E1A07", "0"));
        delBlock.setE1d02(new DataSegmentResponse("E1D02", "5"));
        delBlock.setE1b09(new DataSegmentResponse("E1B09", "0"));
        delBlock.setNdecc03(new DataSegmentResponse("NDECC03", "0"));
        mock.setDelphiBlock(delBlock);


        mock = new CustomerInfo();
        delBlock = new AffordabilityDelphiDataResponse();

        delBlock.setNdspcii(new DataSegmentResponse("NDSPCII", "39"));
        delBlock.setE1b07(new DataSegmentResponse("E1B07", "U"));
        delBlock.setEa1c01(new DataSegmentResponse("EA1C01", "N"));
        delBlock.setE1a07(new DataSegmentResponse("E1A07", "6"));
        delBlock.setE1d02(new DataSegmentResponse("E1D02", "7"));
        delBlock.setE1b09(new DataSegmentResponse("E1B09", "8"));
        delBlock.setNdecc03(new DataSegmentResponse("NDECC03", "0"));
        mock.setDelphiBlock(delBlock);
        mocks.add(mock);
        return Stream.of(mocks);
    }


    static Stream<List<ICustomerInfo>> CreateJsonPropertyMockForFailure() {
        var mocks = new ArrayList<ICustomerInfo>();

        var mock = new CustomerInfo();
        var delBlock = new AffordabilityDelphiDataResponse();

        delBlock.setNdspcii(new DataSegmentResponse("NDSPCII", "-1"));
        delBlock.setE1b07(new DataSegmentResponse("E1B07", "-1"));
        delBlock.setEa1c01(new DataSegmentResponse("EA1C01", "Y"));
        delBlock.setE1a07(new DataSegmentResponse("E1A07", "0"));
        delBlock.setE1d02(new DataSegmentResponse("E1D02", "0"));
        delBlock.setE1b09(new DataSegmentResponse("E1B09", "0"));
        delBlock.setNdecc03(new DataSegmentResponse("NDECC03", "4"));
        mock.setDelphiBlock(delBlock);
        mocks.add(mock);


        mock = new CustomerInfo();
        delBlock = new AffordabilityDelphiDataResponse();

        delBlock.setNdspcii(new DataSegmentResponse("NDSPCII", "40"));
        delBlock.setE1b07(new DataSegmentResponse("E1B07", "2"));
        delBlock.setEa1c01(new DataSegmentResponse("EA1C01", ""));
        delBlock.setE1a07(new DataSegmentResponse("E1A07", "0"));
        delBlock.setE1d02(new DataSegmentResponse("E1D02", "1"));
        delBlock.setE1b09(new DataSegmentResponse("E1B09", "1"));
        delBlock.setNdecc03(new DataSegmentResponse("NDECC03", "2"));
        mock.setDelphiBlock(delBlock);
        mocks.add(mock);

        mock = new CustomerInfo();
        delBlock = new AffordabilityDelphiDataResponse();

        delBlock.setNdspcii(new DataSegmentResponse("NDSPCII", "45"));
        delBlock.setE1b07(new DataSegmentResponse("E1B07", "6"));
        delBlock.setEa1c01(new DataSegmentResponse("EA1C01", "100"));
        delBlock.setE1a07(new DataSegmentResponse("E1A07", "-1"));
        delBlock.setE1d02(new DataSegmentResponse("E1D02", "-6"));
        delBlock.setE1b09(new DataSegmentResponse("E1B09", "3"));
        delBlock.setNdecc03(new DataSegmentResponse("NDECC03", "4"));
        mock.setDelphiBlock(delBlock);
        mocks.add(mock);

        mock = new CustomerInfo();
        delBlock = new AffordabilityDelphiDataResponse();

        delBlock.setNdspcii(new DataSegmentResponse("NDSPCII", "-"));
        delBlock.setE1b07(new DataSegmentResponse("E1B07", "-"));
        delBlock.setEa1c01(new DataSegmentResponse("EA1C01", "-"));
        delBlock.setE1a07(new DataSegmentResponse("E1A07", "-"));
        delBlock.setE1d02(new DataSegmentResponse("E1D02", "-"));
        delBlock.setE1b09(new DataSegmentResponse("E1B09", "-"));
        delBlock.setNdecc03(new DataSegmentResponse("NDECC03", "-"));
        mock.setDelphiBlock(delBlock);
        mocks.add(mock);

        mock = new CustomerInfo();
        delBlock = new AffordabilityDelphiDataResponse();

        delBlock.setNdspcii(new DataSegmentResponse("NDSPCII", " "));
        delBlock.setE1b07(new DataSegmentResponse("E1B07", " "));
        delBlock.setEa1c01(new DataSegmentResponse("EA1C01", " "));
        delBlock.setE1a07(new DataSegmentResponse("E1A07", " "));
        delBlock.setE1d02(new DataSegmentResponse("E1D02", " "));
        delBlock.setE1b09(new DataSegmentResponse("E1B09", " "));
        delBlock.setNdecc03(new DataSegmentResponse("NDECC03", " "));
        mock.setDelphiBlock(delBlock);
        mocks.add(mock);

        mock = new CustomerInfo();
        var nullDelBlock = new AffordabilityDelphiDataResponse();
        mock.setDelphiBlock(nullDelBlock);
        mocks.add(mock);

        return Stream.of(mocks);
    }

	CustomerInfo customerInfo = new CustomerInfo();

	@Test
	@DisplayName("Should Return true when Enhanced Customer Indebtedness Index Condition Matches for Not-Classified Customer")
	void shouldReturnTrueWhenEnhancedCustomerIndebtednessIndexConditionMatchesForNotClassified(){
		customerInfo.setDelphiBlock(new AffordabilityDelphiDataResponse());
		assertTrue(customerInfo.enhancedCustomerIndebtednessIndex());
	}

	@ParameterizedTest
	@MethodSource("createMockDelphiDataResponse")
	@DisplayName("Should Return False when Enhanced Customer Indebtedness Index Conditions does Not Match for Not-Classified Customer")
	void shouldReturnFalseWhenEnhancedCustomerIndebtednessIndexNotMatchesForNotClassified(AffordabilityDelphiDataResponse response){
		this.customerInfo.setDelphiBlock(response);
		assertFalse(customerInfo.enhancedCustomerIndebtednessIndex());
	}

	@Test
	@DisplayName("Should Return true when Worst Status In Last 6 Months Of All Active CAIS Accounts Is Less Than Payment Down Condition Matches for Not-Classified Customer")
	void shouldReturnTrueWhenWorstStatusInLast6MonthsConditionMatchesForNotClassified(){
		customerInfo.setDelphiBlock(new AffordabilityDelphiDataResponse());
		assertTrue(customerInfo.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsLessThanPaymentDown());
	}

	@ParameterizedTest
	@MethodSource("createMockDelphiDataResponse")
	@DisplayName("Should Return False when Worst Status In Last 6 Months Of All Active CAIS Accounts Is Less Than Payment Down Conditions does not Match for Not-Classified Customer")
	void shouldReturnFalseWhenWorstStatusInLast6MonthsConditionsNotMatchesForNotClassified(AffordabilityDelphiDataResponse response){
		this.customerInfo.setDelphiBlock(response);
		assertFalse(customerInfo.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsLessThanPaymentDown());
	}

	@Test
	@DisplayName("Should Return true when worst Current Status Of All Active CAIS Accounts Is Less Than 2 PaymentDown condition matches for Not-Classified Customer")
	void shouldReturnTrueWhenWorstCurrentStatusConditionMatchesForNotClassified(){
		customerInfo.setDelphiBlock(new AffordabilityDelphiDataResponse());
		assertTrue(customerInfo.worstCurrentStatusOfAllActiveCAISAccountsIsLessThan2PaymentDown());
	}

	@ParameterizedTest
	@MethodSource("createMockDelphiDataResponse")
	@DisplayName("Should Return False when worst Current Status Of All Active CAIS Accounts Is Less Than 2 PaymentDown conditions does not match for Not-Classified Customer")
	void shouldReturnFalseWhenWorstCurrentStatusConditionNotMatchesForNotClassified(AffordabilityDelphiDataResponse response){
		this.customerInfo.setDelphiBlock(response);
		assertFalse(customerInfo.worstCurrentStatusOfAllActiveCAISAccountsIsLessThan2PaymentDown());
	}

	@Test
	@DisplayName("Should Return true when detected CCJs Or IVA Or Bankruptcy In Last 6 Year condition matches for Not-Classified Customer")
	void shouldReturnTrueWhenDetectedCCJsOrIVAOrBankruptcyInLast6YearConditionMatchesForNotClassified(){
		customerInfo.setDelphiBlock(new AffordabilityDelphiDataResponse());
		assertTrue(customerInfo.detectedCCJsOrIVAOrBankruptcyInLast6Year());
	}

	@ParameterizedTest
	@MethodSource("createMockDelphiDataResponse")
	@DisplayName("Should Return false when detected CCJs Or IVA Or Bankruptcy In Last 6 Year conditions does not match for Not-Classified Customer")
	void shouldReturnFalseWhenDetectedCCJsOrIVAOrBankruptcyInLast6YearConditionNotMatchesForNotClassified(AffordabilityDelphiDataResponse response){
		this.customerInfo.setDelphiBlock(response);
		assertFalse(customerInfo.detectedCCJsOrIVAOrBankruptcyInLast6Year());
	}

	@ParameterizedTest
	@MethodSource("createMockDelphiDataResponse")
	@DisplayName("Should Return true when Enhanced Customer Indebtedness Index Condition Matches for Non-Prime Customer")
	void shouldReturnTrueWhenEnhancedCustomerIndebtednessIndexConditionMatchesForNonPrime(AffordabilityDelphiDataResponse response){
		this.customerInfo.setDelphiBlock(response);
		assertTrue(customerInfo.enhancedCustomerIndebtednessIndexForNonPrime());
	}

	@Test
	@DisplayName("Should Return False when Enhanced Customer Indebtedness Index Conditions does Not Match for Non-Prime Customer")
	void shouldReturnFalseWhenEnhancedCustomerIndebtednessIndexNotMatchesForNonPrime(){
		customerInfo.setDelphiBlock(new AffordabilityDelphiDataResponse());
		assertFalse(customerInfo.enhancedCustomerIndebtednessIndexForNonPrime());
	}


	@ParameterizedTest
	@MethodSource("createMockDelphiDataResponse")
	@DisplayName("Should Return true worst Status In Last 6 Months Of All Active CAIS Accounts Is Greater Than Payment Down condition matches for Non-Prime Customer")
	void shouldReturnTrueWhenWorstStatusInLast6MonthsConditionMatchesForNonPrime(AffordabilityDelphiDataResponse response){
		this.customerInfo.setDelphiBlock(response);
		assertTrue(customerInfo.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime());
	}

	@Test
	@DisplayName("Should Return False when Worst Status In Last 6 Months Of All Active CAIS Accounts Is Greater Than Payment Down Conditions does not Match for Non-Prime Customer")
	void shouldReturnFalseWhenWorstStatusInLast6MonthsConditionsNotMatchesForNonPrime(){
		customerInfo.setDelphiBlock(new AffordabilityDelphiDataResponse());
		assertFalse(customerInfo.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime());
	}

	@ParameterizedTest
	@MethodSource("createMockDelphiDataResponse")
	@DisplayName("Should Return true when worst Current Status Of All Active CAIS Accounts Is Greater Than Payment Down condition matches for Non-Prime Customer")
	void shouldReturnTrueWhenWorstCurrentStatusConditionMatchesForNonPrime(AffordabilityDelphiDataResponse response){
		this.customerInfo.setDelphiBlock(response);
		assertTrue(customerInfo.worstCurrentStatusOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime());
	}

	@Test
	@DisplayName("Should Return False when worst Current Status Of All Active CAIS Accounts Is Greater Than Payment Down conditions does not match for Non-Prime Customer")
	void shouldReturnFalseWhenWorstCurrentStatusConditionNotMatchesForNonPrime(){
		customerInfo.setDelphiBlock(new AffordabilityDelphiDataResponse());
		assertFalse(customerInfo.worstCurrentStatusOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrime());
	}

	@ParameterizedTest
	@MethodSource("createMockDelphiDataResponse")
	@DisplayName("Should Return true when detected CCJs Or IVA Or Bankruptcy In Last 6 Year condition matches for Not-Classified Customer")
	void shouldReturnTrueWhenDetectedCCJsOrIVAOrBankruptcyInLast6YearConditionMatchesForNonPrime(AffordabilityDelphiDataResponse response){
		this.customerInfo.setDelphiBlock(response);
		assertTrue(customerInfo.detectedCCJsOrIVAOrBankruptcyInLast6YearforNonPrime());
	}

	@Test
	@DisplayName("Should Return false when detected CCJs Or IVA Or Bankruptcy In Last 6 Year conditions does not match for Not-Classified Customer")
	void shouldReturnFalseWhenDetectedCCJsOrIVAOrBankruptcyInLast6YearConditionNotMatchesForNonPrime(){
		customerInfo.setDelphiBlock(new AffordabilityDelphiDataResponse());
		assertFalse(customerInfo.detectedCCJsOrIVAOrBankruptcyInLast6YearforNonPrime());
	}

	static Stream<AffordabilityDelphiDataResponse> createMockDelphiDataResponse(){

		AffordabilityDelphiDataResponse delphiDataResponse = new AffordabilityDelphiDataResponse();
		DataSegmentResponse dataSegmentResponseMock;

		dataSegmentResponseMock = new DataSegmentResponse();
		dataSegmentResponseMock.setValue("40");
		delphiDataResponse.setNdspcii(dataSegmentResponseMock);

		dataSegmentResponseMock = new DataSegmentResponse();
		dataSegmentResponseMock.setValue("2");
		delphiDataResponse.setE1b07(dataSegmentResponseMock);

		dataSegmentResponseMock = new DataSegmentResponse();
		dataSegmentResponseMock.setValue("2");
		delphiDataResponse.setE1b08(dataSegmentResponseMock);

		dataSegmentResponseMock = new DataSegmentResponse();
		dataSegmentResponseMock.setValue("Y");
		delphiDataResponse.setEa1c01(dataSegmentResponseMock);

		return Stream.of(delphiDataResponse);
	}

}
