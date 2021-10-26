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
@Table(name = "room")
public class Room {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private int floor;

	@ManyToOne(fetch = FetchType.LAZY)
	private Location location;

	@OneToMany(cascade = CascadeType.ALL)
	@JoinColumn(name = "room_id")
	private List<Workplace> workplaces;

	@OneToMany(mappedBy = "room")
	private List<Reservation> reservations;
}
