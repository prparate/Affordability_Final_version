package com.affordability.configs;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.retry.support.RetryTemplate;
import org.springframework.web.client.RestClientException;

@Configuration
public class RetryTemplateBeanConfig {

    @Value("${request.max-attempts:3}")
    private Integer maxAttempts;

    @Value("${request.update-cra-attempts:3}")
    private Integer updateCraAttempts;

    @Value("${request.update-cra-backoff-delay-in-seconds:30}")
    private Integer updateCraBackoffDelayInSeconds;


    @Bean
    @Primary
    public RetryTemplate retryTemplate() {
        return RetryTemplate.builder()
                .maxAttempts(maxAttempts)
                .retryOn(RestClientException.class)
                .build();
    }

    @Bean(name = "retryTemplateCraUpdate")
    public RetryTemplate retryTemplateCraUpdate() {
        var fixedDelayBackOffPolicy = updateCraBackoffDelayInSeconds * 1000;
        return RetryTemplate.builder()
                .maxAttempts(updateCraAttempts)
                .fixedBackoff(fixedDelayBackOffPolicy)
                .retryOn(RestClientException.class)
                .build();
    }

}
