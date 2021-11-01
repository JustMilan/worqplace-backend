package com.quintor.worqplace.application.exceptions;

public class WorkplacesNotAvailableException extends RuntimeException {
	public WorkplacesNotAvailableException(int wanted, int available) {
		super("Requested " + wanted + " workplaces but only " + available + " available.");
	}
}
