package com.affordability.exception;

import org.webjars.NotFoundException;

public class CustomerNotFoundException extends NotFoundException {
    public CustomerNotFoundException(String errorMessage) {
        super(errorMessage);
    }
}