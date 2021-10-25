package com.quintor.worqplace.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RoomTest {
	private Long id;
	private int floor;
	private Location location;
	private List<Workplace> workplaces;

	@BeforeEach
	void initialize() {
		this.id = 1L;
		this.floor = 1;
		this.location = new Location("Quintor - Groningen", new Address(11, "A", "HermanStraat", "2958GB", "Groningen"), List.of(new Room()));
		this.workplaces = List.of(new Workplace(), new Workplace());
	}

	@Test
	void shouldCreateRoomCorrectly() {
		assertDoesNotThrow(() -> new Room(id, floor, location, workplaces));
	}
}
