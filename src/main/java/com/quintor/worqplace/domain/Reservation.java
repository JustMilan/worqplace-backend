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

	public Reservation(Long id, LocalDate date, LocalTime startTime, LocalTime endTime, Employee employee, Room room, Workplace workplace) {
		this(date, startTime, endTime, employee, room, workplace);
		this.id = id;
	}

	public Reservation(LocalDate date, LocalTime startTime, LocalTime endTime, Employee employee, Room room, Workplace workplace) {
		if (startTime.isAfter(endTime))
			throw new InvalidStartAndEndTimeException();
		if ((room == null && workplace == null) || (room != null && workplace != null))
			throw new InvalidReservationTypeException();
		this.date = date;
		this.startTime = startTime;
		this.endTime = endTime;
		this.employee = employee;
		this.room = room;
		this.workplace = workplace;
	}
}
