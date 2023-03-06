package com.affordability.customer.regulated.comercial.traders;

import com.affordability.customer.regulated.RegulatedContextDataForTesting;
import com.affordability.customer.regulation.commercial.traders.SoleTraderRegulatedCustomerIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class SoleTraderTest {

	private RegulatedContextDataForTesting contextData;

	@BeforeEach
	void init() {
		this.contextData = new RegulatedContextDataForTesting();
	}

	@Test
	@DisplayName("Customer should be regulated When Policy Value Is Equal to the Policy Limit")
	void customerShouldBeRegulatedWhenPolicyValueIsEqualToThePolicyLimit() {
		double value = 1000;

		contextData.withParams()
					.maxPolicyValueAllowed(value);

		contextData.withCustomer()
					.policyValue(value);

		boolean isRegulated = checkIfIsARegulatedCustomer();

		assertTrue(isRegulated, "Given the Policy Limit Value AND the Customer Policy are: '" + value + "' we expect this should be a regulated customer .");
	}

	@Test
	@DisplayName("Customer should be regulated When Policy Value Is Below to the Policy Limit")
	void customerShouldBeRegulatedWhenPolicyValueIsBelowToThePolicyLimit() {
		double policyValueLimit = 1000;
		double customerPolicyValue = policyValueLimit - 1;

		contextData.withParams()
					.maxPolicyValueAllowed(policyValueLimit);

		contextData.withCustomer()
					.policyValue(customerPolicyValue);

		boolean isRegulated = checkIfIsARegulatedCustomer();

		assertTrue(isRegulated, "Given the Policy Limit Value IS BELOW the Customer Policy, we expect this should be a regulated customer.");
	}

	@Test
	@DisplayName("Customer should be UNregulated When Policy Value Is Greater than to the Policy Limit")
	void customerShouldBeRegulatedWhenPolicyValueIsBelowToTheLimit() {
		double policyValueLimit = 1000;
		double customerPolicyValue = policyValueLimit + 1;

		contextData.withParams()
					.maxPolicyValueAllowed(policyValueLimit);

		contextData.withCustomer()
					.policyValue(customerPolicyValue);

		boolean isRegulated = checkIfIsARegulatedCustomer();

		assertFalse(isRegulated, "Given the Policy Limit Value IS GREATER THAN the Customer Policy, we expect this should be an UNregulated customer.");
	}

	private boolean checkIfIsARegulatedCustomer() {
		var identifier = new SoleTraderRegulatedCustomerIdentifier(this.contextData.getProvider());
		identifier.setCustomerDataForRegulation(this.contextData.getCustomerData());

		var isRegulated = identifier.isThisARegulatedCustomer();

		return isRegulated;
	}

}