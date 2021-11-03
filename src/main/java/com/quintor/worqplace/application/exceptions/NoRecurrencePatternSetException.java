package com.quintor.worqplace.application.exceptions;

/**
 * An exception that is thrown if the
 * {@link com.quintor.worqplace.domain.Recurrence recurrence} is activated
 * but no {@link com.quintor.worqplace.domain.RecurrencePattern} is set
 */
public class NoRecurrencePatternSetException extends RuntimeException {
	public NoRecurrencePatternSetException() {
		super();
	}
}
