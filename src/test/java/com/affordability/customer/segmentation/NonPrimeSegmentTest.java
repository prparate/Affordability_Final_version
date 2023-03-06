package com.affordability.customer.segmentation;

import com.affordability.customer.segmentation.segments.NonPrimeSegment;
import com.affordability.dto.response.AffordabilityDelphiDataResponse;
import com.affordability.service.cra.response.DataSegmentResponse;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class NonPrimeSegmentTest {

	@ParameterizedTest
	@MethodSource("CreateAllNonPrimeMocksWhichReturnInAnAssignableEvaluation")
	@DisplayName("Customer should be assignable to all Non-Prime scenario")
	void customerShouldBeAssignableToAllNonPrimeScenario(List<ICustomerInfo> allAssignableCustomerInfos) {
		var segment = new NonPrimeSegment();
		var index = 0;

		for (ICustomerInfo customer: allAssignableCustomerInfos) {
			var isAssignable = segment.isCustomerAssignableToSegment(customer);

			assertTrue(isAssignable, "Expected CustomerInfo in index '" + index + "' be assignable, check how the mock has been set.");

			index++;
		}
	}

	@Test
	@DisplayName("Customer should NOT be assignable to Non-Prime scenario")
	void customerShouldNotBeAssignableToNonPrimeScenario() {
		var segment = new NonPrimeSegment();

		var isAssignable = segment.isCustomerAssignableToSegment(new CustomerInfoMock());

		assertFalse(isAssignable, "Expected CustomerInfo id non assignable, check how the mock has been set.");

	}

	static Stream<List<ICustomerInfo>> CreateAllNonPrimeMocksWhichReturnInAnAssignableEvaluation(){
		var mocks = new ArrayList<ICustomerInfo>();

		CustomerInfoMock mock;

		mock = new CustomerInfoMock();
		mock.enhancedCustomerIndebtednessIndexForNonPrimeReturn = true;
		mocks.add(mock);

		mock = new CustomerInfoMock();
		mock.worstStatusInLast6MonthsOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrimeReturn = true;
		mocks.add(mock);

		mock = new CustomerInfoMock();
		mock.worstCurrentStatusOfAllActiveCAISAccountsIsGreaterThanPaymentDownforNonPrimeReturn = true;
		mocks.add(mock);

		mock = new CustomerInfoMock();
		mock.detectedCCJsOrIVAOrBankruptcyInLast6YearforNonPrimeReturn = true;
		mocks.add(mock);

		return Stream.of(mocks);
	}


	@ParameterizedTest
	@MethodSource("CreateCustomerInfoListWhichReturnInAnAssignableEvaluation")
	@DisplayName("Should be assignable in every Customer Info scenario")
	void shouldBeAssignableForEveryCustomerInfoScenario(List<ICustomerInfo> allAssignableCustomerInfos) {
		var segment = new NonPrimeSegment();
		var index = 0;

		for (ICustomerInfo customer: allAssignableCustomerInfos) {
			var isAssignable = segment.isCustomerAssignableToSegment(customer);
			assertTrue(isAssignable, "Expected CustomerInfo in index '" + index + "' be assignable, check how the mock has been set.");

			index++;
		}
	}


	@ParameterizedTest
	@MethodSource("CreateAllCustomerInfoWhichReturnNotInAnAssignableEvaluation")
	@DisplayName("Should be assignable in every Customer Info scenario")
	void shouldNotBeAssignableForEveryCustomerInfoScenario(List<ICustomerInfo> allAssignableCustomerInfos) {
		var segment = new NonPrimeSegment();
		var index = 0;

		for (ICustomerInfo customer: allAssignableCustomerInfos) {
			var isAssignable = segment.isCustomerAssignableToSegment(customer);

			assertFalse(isAssignable, "Expected CustomerInfo in index '" + index + "' be assignable, check how the mock has been set.");

			index++;
		}
	}

	static Stream<List<ICustomerInfo>> CreateCustomerInfoListWhichReturnInAnAssignableEvaluation(){
		var mocks = new ArrayList<ICustomerInfo>();

		CustomerInfo mock = new CustomerInfo();
		AffordabilityDelphiDataResponse data = new AffordabilityDelphiDataResponse();

		data.setNdspcii(new DataSegmentResponse("NDSPCII","-3"));
		data.setE1b07(new DataSegmentResponse("E1B07","N"));
		data.setE1b08(new DataSegmentResponse("E1B08","N"));
		data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
		data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
		mock.setDelphiBlock(data);
		mocks.add(mock);

		mock = new CustomerInfo();
		data = new AffordabilityDelphiDataResponse();

		data.setNdspcii(new DataSegmentResponse("NDSPCII","40"));
		data.setE1b07(new DataSegmentResponse("E1B07","N"));
		data.setE1b08(new DataSegmentResponse("E1B08","N"));
		data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
		data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
		mock.setDelphiBlock(data);
		mocks.add(mock);

		mock = new CustomerInfo();
		data = new AffordabilityDelphiDataResponse();

		return Stream.of(mocks);
	}




	static Stream<List<ICustomerInfo>> CreateAllCustomerInfoWhichReturnNotInAnAssignableEvaluation(){
		var mocks = new ArrayList<ICustomerInfo>();

		CustomerInfo mock = new CustomerInfo();
		AffordabilityDelphiDataResponse data = new AffordabilityDelphiDataResponse();

		data.setNdspcii(new DataSegmentResponse("NDSPCII","39"));
		data.setE1b07(new DataSegmentResponse("E1B07","U"));
		data.setE1b08(new DataSegmentResponse("E1B08","N"));
		data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
		data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
		data.setE1a07(new DataSegmentResponse("E1A07","1"));
		data.setE1d02(new DataSegmentResponse("E1d02","1"));
		data.setE1b09(new DataSegmentResponse("E1B09","1"));
		data.setNdecc03(new DataSegmentResponse("NDECC03","1"));
		mock.setDelphiBlock(data);
		mocks.add(mock);

		mock = new CustomerInfo();
		data = new AffordabilityDelphiDataResponse();

		data.setNdspcii(new DataSegmentResponse("NDSPCII","0"));
		data.setE1b07(new DataSegmentResponse("E1B07","U"));
		data.setE1b08(new DataSegmentResponse("E1B08","N"));
		data.setEa1c01(new DataSegmentResponse("EA1C01","N"));
		data.setEa4q05(new DataSegmentResponse("EA4Q05","N"));
		data.setE1a07(new DataSegmentResponse("E1A07","5"));
		data.setE1d02(new DataSegmentResponse("E1d02","1"));
		data.setE1b09(new DataSegmentResponse("E1B09","4"));
		data.setNdecc03(new DataSegmentResponse("NDECC03","2"));
		mock.setDelphiBlock(data);
		mocks.add(mock);


		return Stream.of(mocks);
	}

}