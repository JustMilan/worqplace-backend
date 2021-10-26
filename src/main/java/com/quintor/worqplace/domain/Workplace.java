package com.quintor.worqplace.domain;

import lombok.*;

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
}
