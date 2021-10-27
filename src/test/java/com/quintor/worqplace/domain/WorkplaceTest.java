package com.quintor.worqplace.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WorkplaceTest {
	private Room room;

	@BeforeEach
	void initialize() {
		this.room = new Room();
	}

	@Test
	void shouldCreateCorrectly() {
		assertDoesNotThrow(() -> new Workplace(room));
	}
}
