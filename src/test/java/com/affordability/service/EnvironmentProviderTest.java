package com.affordability.service;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class EnvironmentProviderTest {

    @Autowired
    private IEnvironmentProvider service;


    @DisplayName("should process LIST of Environment [dev,TPFUAT2,TPFUAT,DBDEMO,DBTEST]")
    void testListOfEnvs() {
        assertAll(
                () -> assertThrows(IllegalArgumentException.class, () -> service.setEnvironmentFromHttpRequest("dev")),
                () -> assertDoesNotThrow(() -> service.setEnvironmentFromHttpRequest("TPFUAT2")),
                () -> assertDoesNotThrow(() -> service.setEnvironmentFromHttpRequest("TPFUAT")),
                () -> assertDoesNotThrow(() -> service.setEnvironmentFromHttpRequest("DBDEMO")),
                () -> assertDoesNotThrow(() -> service.setEnvironmentFromHttpRequest("DBTEST"))
        );
    }
}
