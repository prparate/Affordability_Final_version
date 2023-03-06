package com.affordability.customer.regulated.comercial.traders;

import com.affordability.customer.regulation.commercial.traders.LimitedCompanyRegulatedCustomerIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertFalse;

@SpringBootTest
class LimitedCompanyTest {

	@Test
	@DisplayName("Customer should be UNregulated always")
	void customerShouldBeUnregulatedAlways() {

		var identifier = new LimitedCompanyRegulatedCustomerIdentifier();

		var isRegulated = identifier.isThisARegulatedCustomer();

		assertFalse(isRegulated);
	}
}