package com.quintor.worqplace.application.exceptions;

public class WorkplaceNotAvailableException extends RuntimeException {
	public WorkplaceNotAvailableException() {
		super("Workplace is not available");
	}
}
