package com.quintor.worqplace.application.util;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.domain.Recurrence;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.temporal.WeekFields;
import java.util.Locale;

/**
 * Utility class containing functions to verify input dates and times and
 * to calculate if two timeslots are overlapping.
 *
 * @see InvalidStartAndEndTimeException
 * @see InvalidDayException
 * @see com.quintor.worqplace.domain.Room Room
 * @see com.quintor.worqplace.application.RoomService RoomService
 */
public class DateTimeUtils {

	/**
	 * Function that checks if the entered date and times uphold the required standard. If the
	 * start time is after the end time or if the date is before today, an exception is thrown.
	 *
	 * @param date      the input date.
	 * @param startTime the start time on the date.
	 * @param endTime   the end time on the date.
	 * @throws InvalidStartAndEndTimeException when the start time is after the end time,
	 *                                         this exception is thrown.
	 * @throws InvalidDayException             when the entered day is before today,
	 *                                         this exception is thrown.
	 */
	public static void checkReservationDateTime(LocalDate date,
												LocalTime startTime,
	                                            LocalTime endTime) {
		if (startTime.isAfter(endTime))
			throw new InvalidStartAndEndTimeException();

		if (date.isBefore(LocalDate.now()))
			throw new InvalidDayException();
	}

	/**
	 * Function that calculates if two timeslots overlap,
	 * also checks for {@link Recurrence}. It starts by checking
	 * to see if the input dates are on different days, then it checks the
	 * {@link com.quintor.worqplace.domain.RecurrencePattern recurrence patterns}
	 * for overlap and if no different day results from those checks it to see
	 * if the timeslots overlap.
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
	                                       LocalTime existingEndTime, Recurrence recurrence,
	                                       LocalDate newDate, LocalTime newStartTime,
	                                       LocalTime newEndTime) {
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
