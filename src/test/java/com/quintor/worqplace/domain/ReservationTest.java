package com.quintor.worqplace.domain;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidReservationTypeException;
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
	private Workplace workplace;

	@BeforeEach
	void initialize() {
		this.date = LocalDate.now().plusDays(1);
		this.startTime = LocalTime.of(9, 0);
		this.endTime = LocalTime.of(18, 0);
		this.employee = new Employee("Test", "Test");
		this.room = new Room();
		this.workplace = new Workplace();
	}

	@Test
	void shouldCreateReservationCorrectlyWithRoomNull() {
		assertDoesNotThrow(() -> new Reservation(date, startTime, endTime, employee, null, workplace, false));
	}

	@Test
	void shouldCreateReservationCorrectlyWithWorkplaceNull() {
		assertDoesNotThrow(() -> new Reservation(date, startTime, endTime, employee, room, null, false));
	}

	@Test
	void shouldCreateReservationCorrectlyWithCustomId() {
		assertDoesNotThrow(() -> new Reservation(1L, date, startTime, endTime, employee, room, null, false));
	}

	@Test
	void shouldThrowIfRoomAndWorkplaceAreNull() {
		assertThrows(InvalidReservationTypeException.class, () -> new Reservation(date, startTime, endTime, employee, null, null, false));
	}

	@Test
	void shouldThrowIfRoomAndWorkplaceAreNotNull() {
		assertThrows(InvalidReservationTypeException.class, () -> new Reservation(date, startTime, endTime, employee, room, workplace, false));
	}

	@Test
	void ShouldThrowWhenDateIsBeforeToday() {
		assertThrows(InvalidDayException.class, () -> new Reservation(LocalDate.now().minusDays(1), startTime, endTime, employee, null, workplace, false));
	}

	@Test
	void shouldThrowIfEndTimeIsBeforeStartTime() {
		assertThrows(InvalidStartAndEndTimeException.class, () -> new Reservation(date, startTime, startTime.minusMinutes(1), employee, null, workplace, false));
	}
}
