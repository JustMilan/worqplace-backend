package com.quintor.worqplace.domain.exceptions;

/**
 * Exception that is thrown if the location name is not valid
 *
 * @see com.quintor.worqplace.domain.Location#setName(String) name
 */
public class InvalidLocationNameException extends RuntimeException {
	public InvalidLocationNameException() {
		super("Name must start with a capital letter");
	}
}
