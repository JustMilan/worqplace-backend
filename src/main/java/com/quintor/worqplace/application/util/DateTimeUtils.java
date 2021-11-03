package com.quintor.worqplace.application.util;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.domain.Recurrence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

public class DateTimeUtils {

	public static void checkReservationDateTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
		if (startTime.isAfter(endTime))
			throw new InvalidStartAndEndTimeException();

		if (date.isBefore(LocalDate.now()))
			throw new InvalidDayException();
	}

	/**
	 * Function that calculates if two timeslots overlap. Also check with recurring timeslots.
	 *
	 * @param existingDate      date that is already reserved.
	 * @param existingStartTime time from which the existing reservation lasts.
	 * @param existingEndTime   time to which the existing reservation lasts.
	 * @param recurrence        {@link Recurrence} of the existing reservation.
	 * @param newDate           date to compare to the existing date.
	 * @param newStartTime      start time of the to be compared timeslot.
	 * @param newEndTime        end time of the to be compared timeslot.
	 * @return a boolean indicating whether the existing and the new timeslot overlap.
	 */
	public static boolean timeslotsOverlap(LocalDate existingDate, LocalTime existingStartTime,
	                                       LocalTime existingEndTime, Recurrence recurrence, LocalDate newDate,
	                                       LocalTime newStartTime, LocalTime newEndTime) {
		if (!existingDate.equals(newDate) && !recurrence.isActive()) return false;
		if (recurrence.isActive()) {
			switch (recurrence.getRecurrencePattern()) {
				case WEEKLY -> {
					if (!existingDate.getDayOfWeek()
							.equals(newDate.getDayOfWeek())) return false;
				}
				case BIWEEKLY -> {
					WeekFields weekFields = WeekFields.of(Locale.getDefault());
					int oldWeekNumber = existingDate
							.get(weekFields.weekOfWeekBasedYear());
					int newWeekNumber = newDate
							.get(weekFields.weekOfWeekBasedYear());
					if ((newWeekNumber - oldWeekNumber) % 2 > 0 ||
							!existingDate.getDayOfWeek().equals(newDate.getDayOfWeek()))
						return false;
				}
				case MONTHLY -> {
					if (existingDate.getDayOfMonth() != newDate.getDayOfMonth()) return false;
				}
			}
		}
		return !newStartTime.isAfter(existingEndTime) && !newEndTime.isBefore(existingStartTime);
	}
}
