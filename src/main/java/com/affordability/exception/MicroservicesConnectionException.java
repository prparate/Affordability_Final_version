package com.affordability.exception;

import lombok.Getter;
import org.springframework.http.HttpMethod;

import java.io.IOException;

import static java.lang.String.format;

@Getter
public final class MicroservicesConnectionException extends IOException {
    public MicroservicesConnectionException(HttpMethod method, String url, String message) {
        super(format("Request error url: %s method: %s message: %s", method, url, message));
    }
}
