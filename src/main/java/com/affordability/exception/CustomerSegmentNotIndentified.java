package com.affordability.exception;

import org.webjars.NotFoundException;

public class CustomerSegmentNotIndentified extends NotFoundException {

	public CustomerSegmentNotIndentified(String errorMessage) {
		super(errorMessage);
	}
}
