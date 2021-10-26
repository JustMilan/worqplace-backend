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

	public static boolean checkReservationDate(LocalDate date) {
		return date.isBefore(LocalDate.now());
	}
}
