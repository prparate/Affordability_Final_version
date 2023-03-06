package com.affordability.customer.funding;

import com.affordability.service.impl.EnvironmentProvider;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class FundedCustomerTest {

	@MockBean
	private EnvironmentProvider environmentProvider;

	@Autowired
	private FundedCustomer fundedCustomer;

	@Test
	@DisplayName("Should return True when Customer is recognised as Funded Customer")
	void shouldReturnTrueWhenCustomerIsFundedCustomer() throws Exception {

		var envValue = "TPFUAT2";
		assertTrue(fundedCustomer.isFundedCustomer(envValue));
	}

	@Test
	@DisplayName("Should return False when Customer is recognised as Non-Funded Customer")
	void shouldReturnFalseWhenCustomerIsNonFundedCustomer() throws Exception {

		var envValue = "testNF";
		assertFalse(fundedCustomer.isFundedCustomer(envValue));
	}
}
