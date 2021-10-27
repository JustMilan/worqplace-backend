package com.quintor.worqplace.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Entity
@Table(name = "location")
public class Location {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private String name;

	@OneToOne
	private Address address;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "location_id")
	private List<Room> rooms;

	public Location(String name, Address address, List<Room> rooms) {
		setName(name);
		this.address = address;
		this.rooms = rooms;
	}

	public void setName(String name) {
		char[] nameChars = name.strip().toCharArray();

		if (! Character.isUpperCase(nameChars[0]))
			throw new RuntimeException("Name must start with a capital letter");

		this.name = name;
	}
}
