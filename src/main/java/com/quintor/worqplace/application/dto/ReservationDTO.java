package com.quintor.worqplace.application.dto;

import com.quintor.worqplace.domain.Floor;
import com.quintor.worqplace.domain.Reservation;
import com.quintor.worqplace.domain.Timeslot;
import lombok.Getter;

import java.util.List;

@Getter
public class ReservationDTO {
    private final long id;
    private final String employeeName;
    private final String employeeLastName;
    private final String locationName;
    private final Floor roomFloor;
    private final int workplaceNr;
    private final List<Timeslot> timeslots;

    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.employeeName = reservation.getEmployee().getName();
        this.employeeLastName = reservation.getEmployee().getLastName();
        this.locationName = reservation.getWorkplace().getRoom().getLocation().getName();
        this.roomFloor = reservation.getWorkplace().getRoom().getFloor();
        this.workplaceNr = reservation.getWorkplace().getNumber();
        this.timeslots = reservation.getTimeslots();
    }
}
