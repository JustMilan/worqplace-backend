package com.quintor.worqplace.domain;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

class AddressTest {
	private int houseNumber;
	private String addition;
	private String street;
	private String postalCode;
	private String city;

	@BeforeEach
	public void initialize() {
		this.houseNumber = 1;
		this.addition = "A";
		this.street = "TestStreet";
		this.postalCode = "1010AB";
		this.city = "testCity";
	}

	@Test
	void shouldCreateAddressSuccesfully() {
		assertDoesNotThrow(() -> new Address(houseNumber, addition, street, postalCode, city));
	}

	@Test
	void shouldCreateAddressSuccesfullyWithSpaceInStreet() {
		assertDoesNotThrow(() -> new Address(houseNumber, addition, "Test street", postalCode, city));
	}

	@Test
	void shouldThrowWhenInvalidHouseNumber() {
		assertThrows(RuntimeException.class, () -> new Address(-1, addition, street, postalCode, city));
	}

	@Test
	void shouldTrowWhenInvalidAddition() {
		assertThrows(RuntimeException.class, () -> new Address(houseNumber, "-", street, postalCode, city));
	}

	@Test
	void shouldNotThrowWhenAdditionContainsWhiteSpace() {
		assertDoesNotThrow(() -> new Address(houseNumber, "A 4", street, postalCode, city));
	}

	@Test
	void shouldNotThrowWhenHyphenInStreet() {
		assertDoesNotThrow(() -> new Address(houseNumber, addition, "Test-Street", postalCode, city));
	}

	@Test
	void shouldTrowWhenInvalidStreet() {
		assertThrows(RuntimeException.class, () -> new Address(houseNumber, addition, "Test-Street-", postalCode, city));
	}

	@Test
	void shouldThrowWhenStreetContainsWhitespace() {
		assertDoesNotThrow(() -> new Address(houseNumber, addition, "Test Street", postalCode, city));
	}

	@Test
	void shouldThrowWhenStreetContainsInvalidCharacter() {
		assertThrows(RuntimeException.class, () -> new Address(houseNumber, addition, "Test*Street", postalCode, city));
	}

	@Test
	void shouldTrowWhenPostalcodeHasTooManyNumbers() {
		assertThrows(RuntimeException.class, () -> new Address(houseNumber, addition, street, "2211AGA", city));
	}

	@Test
	void shouldTrowWhenPostalcodeHasLettersInDigitPart() {
		assertThrows(RuntimeException.class, () -> new Address(houseNumber, addition, street, "2A11AA", city));
	}

	@Test
	void shouldTrowWhenPostalcodeHasTooManyLetters() {
		assertThrows(RuntimeException.class, () -> new Address(houseNumber, addition, street, "22111A", city));
	}

	@Test
	void shouldTrowWhenPostalcodeHasinvalidCharacters() {
		assertThrows(RuntimeException.class, () -> new Address(houseNumber, addition, street, "2211*A", city));
	}

	@Test
	void shouldTrowWhenCityHasInvalidCharacter() {
		assertThrows(RuntimeException.class, () -> new Address(houseNumber, addition, street, postalCode, "TestC*ty"));
	}

	@Test
	void shouldNotTrowWhenCityHasNumbers() {
		assertDoesNotThrow(() -> new Address(houseNumber, addition, street, postalCode, "TestC1ty"));
	}
}
