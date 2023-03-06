package com.affordability.configs;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springdoc.core.GroupedOpenApi;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration

public class SwaggerBeanConfig {

    @Value("${info.app.version:default}")
    private String version;

    @Bean
    public GroupedOpenApi publicApi() {

        return GroupedOpenApi.builder()
                .group("")
                .packagesToScan("com.affordability.controllers")
                .pathsToMatch("/*")
                .build();
    }

    @Bean
    public OpenAPI springShopOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Affordability Api")
                        .description("Affordability project of PremFina")
                        .version(version));
    }
}
