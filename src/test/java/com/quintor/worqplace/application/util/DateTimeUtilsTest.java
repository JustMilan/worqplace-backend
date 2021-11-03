package com.quintor.worqplace.application.util;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.domain.Recurrence;
import com.quintor.worqplace.domain.RecurrencePattern;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

class DateTimeUtilsTest {

    private LocalDate today;
    private LocalDate tomorrow;
    private LocalDate yesterday;
    private LocalTime nine;
    private LocalTime twelve;
    private LocalTime one;
    private LocalTime four;
    private Recurrence noRecurrence;
    private Recurrence dailyRecurrence;
    private Recurrence weeklyRecurrence;
    private Recurrence biweeklyRecurrence;
    private Recurrence monthlyRecurrence;

    @BeforeEach
    void setUp() {
        this.today = LocalDate.now();
        this.tomorrow = LocalDate.now().plusDays(1);
        this.yesterday = LocalDate.now().minusDays(1);
        this.nine = LocalTime.of(9, 0);
        this.twelve = LocalTime.of(12, 0);
        this.one = LocalTime.of(13, 0);
        this.four = LocalTime.of(16, 0);
        this.noRecurrence = new Recurrence(false, null);
        this.dailyRecurrence = new Recurrence(true, RecurrencePattern.DAILY);
        this.weeklyRecurrence = new Recurrence(true, RecurrencePattern.WEEKLY);
        this.biweeklyRecurrence = new Recurrence(true, RecurrencePattern.BIWEEKLY);
        this.monthlyRecurrence = new Recurrence(true, RecurrencePattern.MONTHLY);
    }

    @Test
    @DisplayName("an exception should be thrown if the start time is after the end time.")
    void checkReservationDateTimeShouldThrowIfStartTimeIsAfterBeginTime() {
        assertThrows(InvalidStartAndEndTimeException.class, () ->
                DateTimeUtils.checkReservationDateTime(this.today, this.twelve, this.nine));
    }

    @Test
    @DisplayName("an exception should be thrown if the date is before today")
    void checkReservationDateTimeShouldThrowIfDateIsBeforeToday() {
        assertThrows(InvalidDayException.class, () ->
                DateTimeUtils.checkReservationDateTime(this.yesterday, this.nine, this.twelve));
    }

    @Test
    @DisplayName("no exception should be thrown if the dates and times are okay")
    void checkReservationDateTimeShouldNotThrowIfDateAndTimeAreOkay() {
        assertDoesNotThrow(() ->
                DateTimeUtils.checkReservationDateTime(this.today, this.nine, this.twelve));
    }

    @Test
    @DisplayName("when there are different input days, the function should return false")
    void timeslotsDontOverlapIfTheyAreOnDifferentDays() {
        assertFalse(DateTimeUtils.timeslotsOverlap(this.today, this.nine,
                this.twelve, this.noRecurrence, this.tomorrow, this.nine, this.twelve));
    }

    @Test
    @DisplayName("when there is recurrence, but the check is for a different time on that day, " +
            "the function should return false")
    void timeslotsDontOverlapIfTheyRecurOnDifferentTimes() {
        assertFalse(DateTimeUtils.timeslotsOverlap(this.today, this.nine,
                this.twelve, this.dailyRecurrence, this.tomorrow, this.one, this.four));

        assertFalse(DateTimeUtils.timeslotsOverlap(this.today, this.nine,
                this.twelve, this.dailyRecurrence, this.tomorrow, this.one, this.four));

        LocalDate nextWeek = LocalDate.now().plusWeeks(1);
        assertFalse(DateTimeUtils.timeslotsOverlap(this.today, this.nine,
                this.twelve, this.weeklyRecurrence, nextWeek, this.one, this.four));

        LocalDate twoWeeks = LocalDate.now().plusWeeks(2);
        assertFalse(DateTimeUtils.timeslotsOverlap(this.today, this.nine,
                this.twelve, this.biweeklyRecurrence, twoWeeks, this.one, this.four));

        twoWeeks = LocalDate.now().plusWeeks(2).plusDays(1);
        assertFalse(DateTimeUtils.timeslotsOverlap(this.today, this.nine,
                this.twelve, this.biweeklyRecurrence, twoWeeks, this.one, this.four));

        LocalDate month = LocalDate.now().plusMonths(1);
        assertFalse(DateTimeUtils.timeslotsOverlap(this.today, this.nine,
                this.twelve, this.monthlyRecurrence, month, this.one, this.four));
    }
}