package com.affordability.customer.regulated.comercial.traders;

import com.affordability.customer.regulation.commercial.traders.CorporateRegulatedCustomerIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class CorporateTest {

	@Test
	@DisplayName("Customer should be always UNregulated")
	void customerShouldBeAlwaysUnregulated() {
		var identifier = new CorporateRegulatedCustomerIdentifier();
		var isRegulated = identifier.isThisARegulatedCustomer();

		assertFalse(isRegulated);
	}
}