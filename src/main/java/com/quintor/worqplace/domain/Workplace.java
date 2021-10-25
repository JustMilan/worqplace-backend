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
@Table(name = "workplace")
public class Workplace {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne(fetch = FetchType.LAZY)
	private Room room;

	@OneToMany(mappedBy = "workplace")
	private List<Reservation> reservations;

	public Workplace(Room room) {
		this.room = room;
	}

	public Workplace(Room room, List<Reservation> reservations) {
		this.room = room;
		this.reservations = reservations;
	}
}
