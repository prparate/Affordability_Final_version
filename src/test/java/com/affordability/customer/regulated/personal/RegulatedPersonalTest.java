package com.affordability.customer.regulated.personal;

import com.affordability.customer.regulation.personal.RegulatedPersonalCustomerIdentifier;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertTrue;

@SpringBootTest
class RegulatedPersonalTest {

    @Test
    @DisplayName("Customer should be regulated always")
    void customerShouldBeUnregulatedAlways() {

        var identifier = new RegulatedPersonalCustomerIdentifier();

        var isRegulated = identifier.isThisARegulatedCustomer();

        assertTrue(isRegulated);
    }
}
