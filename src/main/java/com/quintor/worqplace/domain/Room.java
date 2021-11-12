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

/**
 * Room class, contains data regarding capacity and location.
 *
 * @see Location
 * @see Reservation
 */
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

	/**
	 * Function that counts the amount of reserved workplaces during a specific timeslot.
	 *
	 * @param date      date to check the reserved workplaces on.
	 * @param startTime start time of the timeslot of which to check the reserved workplaces on.
	 * @param endTime   end time of the timeslot of which to check the reserved workplaces on.
	 * @return an int representing the amount of reserved workplaces.
	 */
	public int countReservedWorkspaces(LocalDate date, LocalTime startTime, LocalTime endTime) {
		return this.getReservations().stream().filter(reservation ->
						(date.isAfter(reservation.getDate()) ||
								date.isEqual(reservation.getDate())) &&
								DateTimeUtils.timeslotsOverlap(reservation.getDate(),
										reservation.getStartTime(), reservation.getEndTime(),
										reservation.getRecurrence(), date, startTime, endTime))
				.mapToInt(Reservation::getWorkplaceAmount).sum();
	}

	/**
	 * Function that adds a reservation to the list of reservations.
	 *
	 * @param reservation the reservation that is to be added.
	 */
	public void addReservation(Reservation reservation) {
		ArrayList<Reservation> reservations = new ArrayList<>(this.reservations);
		reservations.add(reservation);
		this.setReservations(reservations);
	}
}
