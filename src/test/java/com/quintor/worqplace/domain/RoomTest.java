package com.quintor.worqplace.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class RoomTest {
	private Long id;
	private int floor;
	private Location location;

	@BeforeEach
	void initialize() {
		this.id = 1L;
		this.floor = 1;
		this.location = new Location("Quintor - Groningen", new Address(11, "A", "HermanStraat", "2958GB", "Groningen"), List.of(new Room()));
	}

	@Test
	@DisplayName("should create a room correctly")
	void shouldCreateRoomCorrectly() {
		assertDoesNotThrow(() -> new Room(id, floor, location, 15, null));
	}

	@Test
	@DisplayName("function should return true if there are no reservations")
	void isWorkplaceRecurrentlyAvailableShouldBeTrueWhenThereAreNoReservations() {
		Room room = new Room(id, floor, location, 15, new ArrayList<>());
		Reservation reservation = new Reservation(LocalDate.now(),
				LocalTime.of(11,11), LocalTime.of(12,12), null, room, 1,
				new Recurrence(true, RecurrencePattern.DAILY));
		assertTrue(room.isWorkplaceRecurrentlyAvailable(reservation));
	}

	@Test
	@DisplayName("function should return true if there are reservations that overlap")
	void isWorkplaceRecurrentlyAvailableShouldBeTrueWhenThereAreReservations() {
		Room room = new Room(id, floor, location, 15, new ArrayList<>());
		Reservation reservation = new Reservation(LocalDate.now().plusDays(1),
				LocalTime.of(11,11), LocalTime.of(12,12), null, room, 1,
				new Recurrence(true, RecurrencePattern.DAILY));
		room.addReservation(reservation);
		Reservation reservation2 = new Reservation(LocalDate.now().plusDays(0),
				LocalTime.of(11,11), LocalTime.of(12,12), null, room, 1,
				new Recurrence(true, RecurrencePattern.DAILY));
		assertTrue(room.isWorkplaceRecurrentlyAvailable(reservation2));
	}

	@Test
	@DisplayName("function should return false if there are reservations that overlap")
	void isWorkplaceRecurrentlyAvailableShouldBeFalseWhenNoPlacesAreAvailable() {
		Room room = new Room(id, floor, location, 15, new ArrayList<>());
		Reservation reservation = new Reservation(LocalDate.now().plusDays(1),
				LocalTime.of(11,11), LocalTime.of(12,12), null, room, 15,
				new Recurrence(true, RecurrencePattern.DAILY));
		room.addReservation(reservation);
		Reservation reservation2 = new Reservation(LocalDate.now().plusDays(0),
				LocalTime.of(11,11), LocalTime.of(12,12), null, room, 1,
				new Recurrence(true, RecurrencePattern.DAILY));
		assertFalse(room.isWorkplaceRecurrentlyAvailable(reservation2));
	}
}
