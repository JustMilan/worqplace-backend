package com.quintor.worqplace.domain;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReservationTest {
	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;
	private Employee employee;
	private Room room;

	@BeforeEach
	void initialize() {
		this.date = LocalDate.now().plusDays(1);
		this.startTime = LocalTime.of(9, 0);
		this.endTime = LocalTime.of(18, 0);
		this.employee = new Employee("Test", "Test");
		this.room = new Room();
	}

	@Test
	void shouldCreateReservationCorrectlyWithRoomNull() {
		assertDoesNotThrow(() -> new Reservation(date, startTime, endTime, employee, null, 15, null));
	}

	@Test
	void shouldCreateReservationCorrectlyWithWorkplaceNull() {
		assertDoesNotThrow(() -> new Reservation(date, startTime, endTime, employee, room, 15, null));
	}

	@Test
	void shouldCreateReservationCorrectlyWithCustomId() {
		assertDoesNotThrow(() -> new Reservation(1L, date, startTime, endTime, employee, room, 15, null));
	}

	@Test
	void ShouldThrowWhenDateIsBeforeToday() {
		assertThrows(InvalidDayException.class, () -> new Reservation(LocalDate.now().minusDays(1), startTime, endTime, employee, null, 15, null));
	}

	@Test
	void shouldThrowIfEndTimeIsBeforeStartTime() {
		assertThrows(InvalidStartAndEndTimeException.class, () -> new Reservation(date, startTime, startTime.minusMinutes(1), employee, null, 15, null));
	}
}
