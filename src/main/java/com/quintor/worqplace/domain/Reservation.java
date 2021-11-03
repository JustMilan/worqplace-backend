package com.quintor.worqplace.domain;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
@NoArgsConstructor
@Entity
@Table(name = "reservation")
public class Reservation {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	private LocalDate date;
	private LocalTime startTime;
	private LocalTime endTime;

	@ManyToOne
	@JoinColumn(name = "employee_id")
	private Employee employee;

	@ManyToOne
	@JoinColumn(name = "room_id")
	private Room room;

	private int workplaceAmount;

	@Embedded
	private Recurrence recurrence;

	/**
	 * @param id        used by Spring | don't use this manually
	 * @param date      reservation date
	 * @param startTime start time of reservation
	 * @param endTime   end time of reservation
	 * @param employee  employee that reserves
	 * @param room      room that is being reserved | null if it is a workplace reservation
	 * @param recurrence handles the {@link Recurrence recurrence} of the reservation
	 * @implNote Is being used by Spring for retrieving reservations
	 */
	public Reservation(Long id, LocalDate date, LocalTime startTime, LocalTime endTime, Employee employee, Room room, int amount, Recurrence recurrence) {
		this(date, startTime, endTime, employee, room, amount, recurrence);
		this.id = id;
	}

	public Reservation(LocalDate date, LocalTime startTime, LocalTime endTime, Employee employee, Room room, int amount, Recurrence recurrence) {
		if (startTime.isAfter(endTime))
			throw new InvalidStartAndEndTimeException();

		setDate(date);
		this.startTime = startTime;
		this.endTime = endTime;
		this.employee = employee;
		this.room = room;
		this.workplaceAmount = amount;
		this.recurrence = recurrence;
	}

	public void setDate(LocalDate date) {
		if (date.isBefore(LocalDate.now()))
			throw new InvalidDayException();

		this.date = date;
	}

	@Override
	public String toString() {
		return "Reservation{" +
				"id=" + id +
				", date=" + date +
				", startTime=" + startTime +
				", endTime=" + endTime +
				", employee=" + employee.getId() +
				", room=" + room.getId() +
				", workplaceAmount=" + workplaceAmount +
				", recurring=" + recurrence +
				'}';
	}
}
