package com.affordability.customer.regulated.comercial.traders;

import com.affordability.customer.regulated.RegulatedContextDataForTesting;
import com.affordability.customer.regulation.commercial.traders.PartnershipRegulatedCustomerIdentifier;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class PartnershipTest {

	private RegulatedContextDataForTesting contextData;

	@BeforeEach
	void init() {
		this.contextData = new RegulatedContextDataForTesting();
	}

	@Test
	@DisplayName("Customer should be regulated When Policy Value And Number Of Partners are in the Limit")
	void customerShouldBeRegulatedWhenPolicyValueAndNumberOfPartnersAreInTheLimit() {
		double policyValue = 1000;
		int maxNumberOfPartners = 10;

		contextData.withParams()
					.maxPolicyValueAllowed(policyValue)
				    .maxNumberOfPartnersAllowed(maxNumberOfPartners);

		contextData.withCustomer()
					.policyValue(policyValue)
					.numberOfPartners(maxNumberOfPartners);

		boolean isRegulated = checkIfIsARegulatedCustomer();

		assertTrue(isRegulated);
	}

	@Test
	@DisplayName("Customer should be UNregulated When Number Of Partners is Greater Than the Limit")
	void customerShouldBeUnregulatedWhenNumberOfPartnersIsGreaterThanTheLimit() {
		double policyValueLimit = 1000;
		int maxNumberOfPartners = 10;
		int customerNumberOfPartners = maxNumberOfPartners + 1;

		contextData.withParams()
					.maxPolicyValueAllowed(policyValueLimit)
					.maxNumberOfPartnersAllowed(maxNumberOfPartners);

		contextData.withCustomer()
					.policyValue(policyValueLimit)
					.numberOfPartners(customerNumberOfPartners);

		boolean isRegulated = checkIfIsARegulatedCustomer();

		assertFalse(isRegulated);
	}

	@Test
	@DisplayName("Customer should be UNregulated When Policy is Greater Than the Limit")
	void customerShouldBeUnregulatedWhenPolicyIsGreaterThanTheLimit() {
		double policyValueLimit = 1000;
		double customerPolicyValue = policyValueLimit + 1;
		int maxNumberOfPartners = 10;
		int customerNumberOfPartners = maxNumberOfPartners;

		contextData.withParams()
				.maxPolicyValueAllowed(policyValueLimit)
				.maxNumberOfPartnersAllowed(maxNumberOfPartners);

		contextData.withCustomer()
				.policyValue(customerPolicyValue)
				.numberOfPartners(customerNumberOfPartners);

		boolean isRegulated = checkIfIsARegulatedCustomer();

		assertFalse(isRegulated);
	}

	private boolean checkIfIsARegulatedCustomer() {
		var identifier = new PartnershipRegulatedCustomerIdentifier(this.contextData.getProvider());
		identifier.setCustomerDataForRegulation(this.contextData.getCustomerData());

		var isRegulated = identifier.isThisARegulatedCustomer();

		return isRegulated;
	}

}