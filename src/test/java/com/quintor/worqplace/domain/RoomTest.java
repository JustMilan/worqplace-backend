package com.quintor.worqplace.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class RoomTest {
	private long id;
	private int floor;
	private List<Workplace> workplaces;

	@BeforeEach
	void initialize() {
		this.id = 1L;
		this.floor = 1;
		this.workplaces = List.of(new Workplace(), new Workplace());
	}

	@Test
	void shouldCreateRoomCorrectly() {
		assertDoesNotThrow(() -> new Room(id, floor, workplaces));
	}
}
