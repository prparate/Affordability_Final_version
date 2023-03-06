package com.affordability.customer.regulated.comercial.traders;

import com.affordability.customer.regulated.RegulatedContextDataForTesting;
import com.affordability.customer.regulation.commercial.traders.RegisteredCharityRegulatedCustomerIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RegisteredCharityTest {

	private RegulatedContextDataForTesting contextData;

	@BeforeEach
	void init() {
		this.contextData = new RegulatedContextDataForTesting();
	}

	@Test
	@DisplayName("Customer should be regulated When Policy Value is in the Limit")
	void customerShouldBeRegulatedWhenPolicyValueIsInTheLimit() {
		double policyValue = 1000;

		contextData.withParams()
					.maxPolicyValueAllowed(policyValue);

		contextData.withCustomer()
					.policyValue(policyValue);

		boolean isRegulated = checkIfIsARegulatedCustomer();

		assertTrue(isRegulated);
	}

	@Test
	@DisplayName("Customer should be UNregulated When Policy is Greater Than the Limit")
	void customerShouldBeUnregulatedWhenPolicyIsGreaterThanTheLimit() {
		double policyValueLimit = 1000;
		double customerPolicyValue = policyValueLimit + 1;

		contextData.withParams()
				.maxPolicyValueAllowed(policyValueLimit);

		contextData.withCustomer()
				.policyValue(customerPolicyValue);

		boolean isRegulated = checkIfIsARegulatedCustomer();

		assertFalse(isRegulated);
	}

	private boolean checkIfIsARegulatedCustomer() {
		var identifier = new RegisteredCharityRegulatedCustomerIdentifier(this.contextData.getProvider());
		identifier.setCustomerDataForRegulation(this.contextData.getCustomerData());

		var isRegulated = identifier.isThisARegulatedCustomer();

		return isRegulated;
	}

}