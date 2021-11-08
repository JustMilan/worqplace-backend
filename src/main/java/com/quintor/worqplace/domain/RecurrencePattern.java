package com.quintor.worqplace.domain;

/**
 * A possible pattern of recurrence, can be either every day, every week on the
 * same day, once every two weeks or once a month.
 * @see Recurrence
 * @see Reservation
 */
public enum RecurrencePattern {
	DAILY,
	WEEKLY,
	BIWEEKLY,
	MONTHLY
}
