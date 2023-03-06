package com.affordability.customer.regulated.comercial.traders;

import com.affordability.customer.regulation.commercial.traders.LimitedLiabilityPartnershipRegulatedCustomerIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class LimitedLiabilityPartnershipTest {

	@Test
	@DisplayName("Customer should be UNregulated always")
	void customerShouldBeUnregulatedAlways() {

		var identifier = new LimitedLiabilityPartnershipRegulatedCustomerIdentifier();

		var isRegulated = identifier.isThisARegulatedCustomer();

		assertFalse(isRegulated);
	}
}