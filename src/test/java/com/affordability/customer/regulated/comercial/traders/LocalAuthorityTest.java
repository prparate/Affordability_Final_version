package com.affordability.customer.regulated.comercial.traders;

import com.affordability.customer.regulation.commercial.traders.LocalAuthorityRegulatedCustomerIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class LocalAuthorityTest {

	@Test
	@DisplayName("Customer should be UNregulated always")
	void customerShouldBeUnregulatedAlways() {

		var identifier = new LocalAuthorityRegulatedCustomerIdentifier();

		var isRegulated = identifier.isThisARegulatedCustomer();

		assertFalse(isRegulated);
	}
}