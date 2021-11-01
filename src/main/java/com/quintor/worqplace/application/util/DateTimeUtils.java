package com.quintor.worqplace.application.util;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;

import java.time.LocalDate;
import java.time.LocalTime;

public class DateTimeUtils {

	public static void checkReservationDateTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
		if (startTime.isAfter(endTime))
			throw new InvalidStartAndEndTimeException();

		if (date.isBefore(LocalDate.now()))
			throw new InvalidDayException();
	}

	public static boolean timeslotsOverlap(LocalDate existingDate, LocalTime existingStartTime,
	                                       LocalTime existingEndTime, LocalDate newDate,
	                                       LocalTime newStartTime, LocalTime newEndTime) {
		if (!existingDate.equals(newDate)) return false;
		return !newStartTime.isAfter(existingEndTime) && !newEndTime.isBefore(existingStartTime);
	}

}
