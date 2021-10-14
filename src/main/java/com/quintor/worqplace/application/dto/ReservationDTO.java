package com.quintor.worqplace.application.dto;

import com.quintor.worqplace.domain.Reservation;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
public class ReservationDTO {
    private final Long id;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final Long employeeId;
    private Long roomId;
    private Long workplaceId;

    public ReservationDTO(Reservation reservation) {
        this.id = reservation.getId();
        this.date = reservation.getDate();
        this.startTime = reservation.getStartTime();
        this.endTime = reservation.getEndTime();
        this.employeeId = reservation.getEmployee().getId();
        if (reservation.getRoom() != null)
            this.roomId = reservation.getRoom().getId();
        if (reservation.getWorkplace() != null)
        this.workplaceId = reservation.getWorkplace().getId();
    }
}