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
@Table(name = "employee")
public class Employee {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String firstName;
	private String lastName;

	public Employee(String firstName, String lastName) {
		setFirstName(firstName);
		setLastName(lastName);
	}

	public void setFirstName(String firstName) {
		char[] firstNameChars = firstName.toCharArray();

		if (! Character.isUpperCase(firstNameChars[0]))
			throw new RuntimeException("Name must start with a capital letter");

		for (char c : firstNameChars)
			if (! Character.isLetter(c))
				throw new RuntimeException("first name must consist letters only");


		this.firstName = firstName;
	}

	public void setLastName(String lastName) {
		char[] lastNameChars = lastName.toCharArray();

		if (! Character.isUpperCase(lastNameChars[0]))
			throw new RuntimeException("Name must start with a capital letter");

		for (char c : lastNameChars)
			if (! Character.isLetter(c))
				throw new RuntimeException("last name must consist letters only");

		this.lastName = lastName;
	}
}
