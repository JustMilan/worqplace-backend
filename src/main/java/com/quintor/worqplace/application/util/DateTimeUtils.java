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

	public static boolean timeslotsOverlap(LocalDate existingDate, LocalTime existingStartTime,
	                                       LocalTime existingEndTime, Recurrence recurrence, LocalDate newDate,
	                                       LocalTime newStartTime, LocalTime newEndTime) {
		if (!existingDate.equals(newDate)) return false;
		if (recurrence.isActive()) {
			switch (recurrence.getRecurrencePattern()) {
				case WEEKLY -> {
					if (!existingDate.getDayOfWeek().equals(
							newDate.getDayOfWeek())) return false;
				}
				case BIWEEKLY -> {
					WeekFields weekFields = WeekFields.of(Locale.getDefault());
					int oldWeekNumber = existingDate.get
							(weekFields.weekBasedYear());
					int newWeekNumber = newDate.get
							(weekFields.weekBasedYear());
					if ((newWeekNumber - oldWeekNumber) % 2 > 0) return false;
					if (!existingDate.getDayOfWeek().equals(newDate.getDayOfWeek())) return false;
				}
				case MONTHLY -> {
					if (existingDate.getDayOfMonth() != newDate.getDayOfMonth()) return false;
				}
			}
		}
		return !newStartTime.isAfter(existingEndTime) && !newEndTime.isBefore(existingStartTime);
	}
}
