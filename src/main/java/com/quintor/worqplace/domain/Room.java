package com.quintor.worqplace.domain;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
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
