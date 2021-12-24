package com.quintor.worqplace.domain;

import com.quintor.worqplace.application.exceptions.RoomNotAvailableException;
import com.quintor.worqplace.application.exceptions.WorkplacesNotAvailableException;
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
import java.util.stream.Collectors;

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
	 * Function that checks if the requested amount of workplaces is available.
	 *
	 * @param reservation Reservation for which to check the availability.
	 * @return a boolean indicating the availability.
	 */
	public boolean isWorkplaceRecurrentlyAvailable(Reservation reservation) {
		if (reservation.getRecurrence().isActive()) {
			var total = 0;
			for (Reservation existingReservation : this.getReservations()) {
				for (Reservation reservation1 : this.getReservationsThatOverlap(
						existingReservation.getDate(), existingReservation.getStartTime(),
						existingReservation.getEndTime())) {
					total += DateTimeUtils.timeslotsOverlap(reservation.getDate(),
							reservation.getStartTime(), reservation.getEndTime(),
							reservation.getRecurrence(), reservation1.getDate(),
							reservation1.getStartTime(), reservation1.getEndTime())
							? reservation1.getWorkplaceAmount() : 0;
				}
				if ((total + reservation.getWorkplaceAmount()) > this.capacity) {
					return false;
				}
				total = 0;
			}
		}
		return true;
	}

	/**
	 * Function that adds a reservation to the list of reservations.
	 *
	 * @param reservation the reservation that is to be added.
	 */
	public void addReservation(Reservation reservation) {
		int wanted = reservation.getWorkplaceAmount();
		int available = this.capacity - countReservedWorkspaces(reservation.getDate(),
				reservation.getStartTime(), reservation.getEndTime());
		if (wanted <= available) {
			if (isWorkplaceRecurrentlyAvailable(reservation)) {
				var reservationsClone = new ArrayList<>(this.reservations);
				reservationsClone.add(reservation);
				this.setReservations(reservationsClone);
			} else throw new RoomNotAvailableException();
		} else throw new WorkplacesNotAvailableException(wanted, available);
	}

	public List<Reservation> getReservationsThatOverlap(LocalDate date,
	                                                    LocalTime startTime, LocalTime endTime) {
		return this.reservations.stream()
				.filter(reservation ->
						DateTimeUtils.timeslotsOverlap(reservation.getDate(),
								reservation.getStartTime(), reservation.getEndTime(),
								reservation.getRecurrence(), date,
								startTime, endTime))
				.collect(Collectors.toUnmodifiableList());
	}
}
