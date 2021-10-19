package com.quintor.worqplace.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EmployeeTest {
	private String firstName;
	private String lastName;

	@BeforeEach
	void initialize() {
		this.firstName = "TestFirstName";
		this.lastName = "TestLastName";
	}

	@Test
	void shouldNotThrowWhenCorrectEmployee() {
		assertDoesNotThrow(() -> new Employee(firstName, lastName));
	}

	@Test
	void shouldThrowWhenFirstNameDoesNotStartWithCapitalLetter() {
		assertThrows(RuntimeException.class, () -> new Employee("testFirstName", lastName));
	}

	@Test
	void shouldThrowWhenLastNameDoesNotStartWithCapitalLetter() {
		assertThrows(RuntimeException.class, () -> new Employee(firstName, "testLastName"));
	}

	@Test
	void shouldThrowWhenFirstNameContainsDigits() {
		assertThrows(RuntimeException.class, () -> new Employee("test3firstName", lastName));
	}

	@Test
	void shouldThrowWhenLastNameContainsDigits() {
		assertThrows(RuntimeException.class, () -> new Employee(firstName, "l0stName"));
	}

	@Test
	void shouldThrowWhenFirstNameContainsSymbols() {
		assertThrows(RuntimeException.class, () -> new Employee("FirstN@me", lastName));
	}

	@Test
	void shouldThrowWhenLastNameContainsSymbols() {
		assertThrows(RuntimeException.class, () -> new Employee(firstName, "L^astName"));
	}
}
