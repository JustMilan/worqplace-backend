package com.quintor.worqplace.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

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
}
