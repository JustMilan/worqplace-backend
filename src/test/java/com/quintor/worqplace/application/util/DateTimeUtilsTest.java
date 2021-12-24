package com.quintor.worqplace.application.util;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.domain.Recurrence;
import com.quintor.worqplace.domain.RecurrencePattern;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static com.quintor.worqplace.application.util.DateTimeUtils.checkReservationDateTime;
import static com.quintor.worqplace.application.util.DateTimeUtils.timeslotsOverlap;
import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilsTest {

	private static final LocalDate TODAY = LocalDate.now();
	private static final LocalDate TOMORROW = LocalDate.now().plusDays(1);
	private static final LocalDate YESTERDAY = LocalDate.now().minusDays(1);
	private static final LocalTime NINE = LocalTime.of(9, 0);
	private static final LocalTime TWELVE = LocalTime.of(12, 0);
	private static final LocalTime ONE = LocalTime.of(13, 0);
	private static final LocalTime FOUR = LocalTime.of(16, 0);
	private static final Recurrence NO_RECURRENCE = new Recurrence(false, RecurrencePattern.NONE);
	private static final Recurrence DAILY_RECURRENCE = new Recurrence(true, RecurrencePattern.DAILY);
	private static final Recurrence WEEKLY_RECURRENCE = new Recurrence(true, RecurrencePattern.WEEKLY);
	private static final Recurrence BIWEEKLY_RECURRENCE = new Recurrence(true, RecurrencePattern.BIWEEKLY);
	private static final Recurrence MONTHLY_RECURRENCE = new Recurrence(true, RecurrencePattern.MONTHLY);

	@Test
	@DisplayName("an exception should be thrown if the start time is after the end time.")
	void checkReservationDateTimeShouldThrowIfStartTimeIsAfterBeginTime() {
		assertThrows(InvalidStartAndEndTimeException.class,
				() -> checkReservationDateTime(TODAY, TWELVE, NINE));
	}

	@Test
	@DisplayName("an exception should be thrown if the date is before today")
	void checkReservationDateTimeShouldThrowIfDateIsBeforeToday() {
		assertThrows(InvalidDayException.class,
				() -> checkReservationDateTime(YESTERDAY, NINE, TWELVE));
	}

	@Test
	@DisplayName("no exception should be thrown if the dates and times are okay")
	void checkReservationDateTimeShouldNotThrowIfDateAndTimeAreOkay() {
		assertDoesNotThrow(() -> checkReservationDateTime(TODAY, NINE, TWELVE));
	}

	@Test
	@DisplayName("when there are different input days, the function should return false")
	void timeslotsDontOverlapIfTheyAreOnDifferentDays() {
		assertFalse(timeslotsOverlap(TODAY, NINE, TWELVE, NO_RECURRENCE, TOMORROW, NINE, TWELVE));
	}

	//TODO: split up into multiple tests
	@Test
	@DisplayName("when there is recurrence, but the check is for a different time on that day, " +
			"the function should return false")
	void timeslotsDontOverlapIfTheyRecurOnDifferentTimes() {
		assertFalse(timeslotsOverlap(TODAY, NINE, TWELVE, DAILY_RECURRENCE, TOMORROW, ONE, FOUR));

		assertFalse(timeslotsOverlap(TODAY, NINE, TWELVE, DAILY_RECURRENCE, TOMORROW, ONE, FOUR));

		LocalDate nextWeek = LocalDate.now().plusWeeks(1);
		assertFalse(timeslotsOverlap(TODAY, NINE, TWELVE, WEEKLY_RECURRENCE, nextWeek, ONE, FOUR));

		LocalDate twoWeeks = LocalDate.now().plusWeeks(2);
		assertFalse(timeslotsOverlap(TODAY, NINE, TWELVE, BIWEEKLY_RECURRENCE, twoWeeks, ONE, FOUR));

		twoWeeks = LocalDate.now().plusWeeks(2).plusDays(1);
		assertFalse(timeslotsOverlap(TODAY, NINE, TWELVE, BIWEEKLY_RECURRENCE, twoWeeks, ONE, FOUR));

		LocalDate month = LocalDate.now().plusMonths(1);
		assertFalse(timeslotsOverlap(TODAY, NINE, TWELVE, MONTHLY_RECURRENCE, month, ONE, FOUR));

		assertTrue(timeslotsOverlap(LocalDate.now().plusDays(1),
				LocalTime.of(11, 11), LocalTime.of(12, 12),
				new Recurrence(true, RecurrencePattern.DAILY),
				LocalDate.now().plusDays(0), LocalTime.of(11, 11), LocalTime.of(12, 12)));
	}
}
