package com.quintor.worqplace.domain;

import com.quintor.worqplace.application.exceptions.InvalidReservationTypeException;
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

	@ManyToOne
	@JoinColumn(name = "workplace_id")
	private Workplace workplace;

	private boolean recurring;

	/**
	 * @param id        used by Spring | don't use this manually
	 * @param date      reservation date
	 * @param startTime start time of reservation
	 * @param endTime   end time of reservation
	 * @param employee  employee that reserves
	 * @param room      room that is being reserved | null if it is a workplace reservation
	 * @param workplace workplace that is being reserved | null if it is a room reservation
	 * @param recurring Is the reservation recurring
	 * @implNote Is being used by Spring for retrieving reservations
	 */
	public Reservation(Long id, LocalDate date, LocalTime startTime, LocalTime endTime, Employee employee, Room room, Workplace workplace, boolean recurring) {
		this(date, startTime, endTime, employee, room, workplace, recurring);
		this.id = id;
	}

	public Reservation(LocalDate date, LocalTime startTime, LocalTime endTime, Employee employee, Room room, Workplace workplace, boolean recurring) {
		if (startTime.isAfter(endTime))
			throw new InvalidStartAndEndTimeException();
		if ((room == null && workplace == null) || (room != null && workplace != null))
			throw new InvalidReservationTypeException();

		setDate(date);
		setStartTime(startTime);
		setEndTime(endTime);
		this.employee = employee;
		this.room = room;
		this.workplace = workplace;
		this.recurring = recurring;
	}

	public void setDate(LocalDate date) {
		if (date.isBefore(LocalDate.now()))
			throw new InvalidStartAndEndTimeException();

		this.date = date;
	}

	public void setEndTime(LocalTime endTime) {
		if (endTime.isBefore(startTime))
			throw new InvalidStartAndEndTimeException();

		this.endTime = endTime;
	}

	@Override
	public String toString() {
		return "Reservation{" +
				"employee=" + employee +
				", room=" + room +
				", workplace=" + workplace +
				", recurring=" + recurring +
				'}';
	}
}
