package com.quintor.worqplace;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;

class WorqplaceApplicationTests {

	@Test
	void contextLoads() {
		assertDoesNotThrow(() -> WorqplaceApplication.main(new String[]{}));
	}
}
