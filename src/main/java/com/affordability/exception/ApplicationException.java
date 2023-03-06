package com.affordability.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
public class ApplicationException extends Exception {
    private final String agreementNumber;
    private final HttpStatus httpStatus;

    private static final String DEFAULT_MESSAGE = "[agreement_number: %s]: %s";

    public ApplicationException(String agreementNumber, String message) {
        super(String.format(DEFAULT_MESSAGE, agreementNumber, message));
        this.agreementNumber = agreementNumber;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApplicationException(String agreementNumber, String message,HttpStatus status) {
        super(String.format(DEFAULT_MESSAGE, agreementNumber, message));
        this.agreementNumber = agreementNumber;
        this.httpStatus = status;
    }
    public ApplicationException(String agreementNumber, Throwable throwable) {
        super(String.format(DEFAULT_MESSAGE, agreementNumber, throwable.getMessage()), throwable);
        this.agreementNumber = agreementNumber;
        this.httpStatus = HttpStatus.INTERNAL_SERVER_ERROR;
    }

    public ApplicationException(String agreementNumber, Throwable throwable, HttpStatus httpStatus, String message) {
        super(String.format(DEFAULT_MESSAGE, agreementNumber, message), throwable);
        this.agreementNumber = agreementNumber;
        this.httpStatus = httpStatus;
    }
    public ApplicationException(String agreementNumber, Throwable throwable, HttpStatus httpStatus) {
        super(String.format(DEFAULT_MESSAGE, agreementNumber,throwable.getMessage()), throwable);
        this.agreementNumber = agreementNumber;
        this.httpStatus = httpStatus;
    }
}
