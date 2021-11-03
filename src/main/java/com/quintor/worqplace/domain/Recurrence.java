package com.quintor.worqplace.domain;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.quintor.worqplace.application.exceptions.NoRecurrencePatternSetException;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.Embeddable;

/**
 * The recurrence of the {@link Reservation reservation}, manages if it is
 * active and on what {@link RecurrencePattern pattern} it is set.
 * @see RecurrencePattern
 * @see Reservation
 */
@Embeddable
@Setter
@Getter
@NoArgsConstructor
@EqualsAndHashCode
public class Recurrence {
	private boolean active;
	private RecurrencePattern recurrencePattern;

	/**
	 * Constructor of the {@link Recurrence recurrence} class.
	 *
	 * @param active            whether the reservation is recurring.
	 * @param recurrencePattern {@link RecurrencePattern} the pattern of the recurrence.
	 */
	@JsonCreator
	public Recurrence(boolean active, RecurrencePattern recurrencePattern) {
		this.setRecurrencePattern(recurrencePattern);
		this.setActive(active);
	}

	/**
	 * Update if the reservation is recurring. Throws an
	 * {@link NoRecurrencePatternSetException exception} when no
	 * {@link RecurrencePattern recurrence pattern} has been set.
	 *
	 * @param active whether the recurrence is active.
	 */
	public void setActive(boolean active) {
		this.active = active && this.recurrencePattern != null;
		if (!this.active) this.recurrencePattern = null;
	}
}
