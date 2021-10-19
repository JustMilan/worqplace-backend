package com.quintor.worqplace.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Getter
@Table(name = "address")
public class Address {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int houseNumber;
	private String addition;
	private String street;
	private String postalCode;
	private String city;

	public Address(int houseNumber, String addition, String street, String postalCode, String city) {
		setHouseNumber(houseNumber);
		setAddition(addition);
		setStreet(street);
		setPostalCode(postalCode);
		setCity(city);
	}

	public void setHouseNumber(int number) {
		char[] numberChars = String.valueOf(number).toCharArray();
		for (char c : numberChars) {
			if (! Character.isDigit(c))
				throw new RuntimeException("HouseNumber must consist of (positive) numbers only");
		}

		this.houseNumber = number;
	}

	public void setAddition(String addition) {
		char[] additionChars = addition.toCharArray();
		for (char c : additionChars)
			if (! Character.isLetterOrDigit(c))
				throw new RuntimeException("Addition must consist of numbers and letters only");

		this.addition = addition;
	}

	public void setStreet(String street) {
		char[] streetChars = street.toCharArray();
		int listSize = streetChars.length;
		for (int i = 0; i < streetChars.length; i++) {
			char c = streetChars[i];
			if (i == listSize - 1 && ! Character.isLetterOrDigit(c))
				throw new RuntimeException("Streetname must end with a letter or a number");

			if (c != '-' && ! Character.isLetterOrDigit(c) && ! Character.isWhitespace(c))
				throw new RuntimeException("Streetname must consist of numbers and letters only");
		}

		this.street = street;
	}

	public void setPostalCode(String postalCode) {
		char[] postalCodeChars = postalCode.toCharArray();

		if (postalCodeChars.length > 6)
			throw new RuntimeException("Invalid postalcode length");

		for (int i = 0; i < postalCodeChars.length; i++) {
			char c = postalCodeChars[i];
			if (i < 4) {
				if (! Character.isDigit(c))
					throw new RuntimeException("First four characters of postalcode must consist of numbers only");
			} else if (! Character.isLetter(c))
				throw new RuntimeException("Last two characters of postalcode must consist of letters only");
		}

		this.postalCode = postalCode;
	}

	public void setCity(String city) {
		char[] cityChars = city.toCharArray();
		for (char c : cityChars)
			if (! Character.isLetterOrDigit(c))
				throw new RuntimeException("City name must consist of only letters");

		this.city = city;
	}
}
