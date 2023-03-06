package com.affordability.customer.funding;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.util.stream.Stream;

import static java.util.Arrays.stream;

@RequiredArgsConstructor
@Service
public class FundedCustomer implements IFundedCustomer {
    @Value("${premfina.environment.funded-customer}")
    private String fundedCustomerEnv;

    @Override
    public boolean
    isFundedCustomer(String environment) {
        var fundedEnvironmentArray = stream(fundedCustomerEnv.split(","))
                .map(String::trim);
        return checkIfEnvironmentMatches(fundedEnvironmentArray, environment);
    }

    private boolean checkIfEnvironmentMatches(Stream<String> environmentArray, String environment) {
        return environmentArray
                .anyMatch(env -> env.equalsIgnoreCase(environment));
    }
}
