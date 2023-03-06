package com.affordability.customer.regulated.comercial.traders;

import com.affordability.customer.regulation.commercial.traders.SchoolRegulatedCustomerIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class SchoolTraderTest {

	@Test
	@DisplayName("Customer should be always UNregulated")
	void customerShouldBeAlwaysUnregulated() {
		var identifier = new SchoolRegulatedCustomerIdentifier();
		var isRegulated = identifier.isThisARegulatedCustomer();

		assertFalse(isRegulated);
	}
}