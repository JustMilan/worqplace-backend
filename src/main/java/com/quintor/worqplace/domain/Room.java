package com.quintor.worqplace.domain;

import com.quintor.worqplace.application.util.DateTimeUtils;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
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

	private int capacity;

	@OneToMany(mappedBy = "room")
	private List<Reservation> reservations;

	public int countReservedWorkspaces(LocalDate date, LocalTime startTime, LocalTime endTime) {
		return this.getReservations().stream().filter(reservation -> DateTimeUtils.timeslotsOverlap(
				reservation.getDate(), reservation.getStartTime(), reservation.getEndTime(),
				date, startTime, endTime)).mapToInt(Reservation::getWorkplaceAmount).sum();
	}

	public void addReservation(Reservation reservation) {
		ArrayList<Reservation> reservations = new ArrayList<>(this.reservations);
		reservations.add(reservation);
		this.setReservations(reservations);
	}
}
