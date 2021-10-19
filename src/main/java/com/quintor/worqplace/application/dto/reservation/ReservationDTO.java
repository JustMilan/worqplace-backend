package com.quintor.worqplace.application.dto.reservation;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.time.LocalTime;

@Getter
@Setter
public class ReservationDTO {
    private Long id;
    private LocalDate date;
    private LocalTime startTime;
    private LocalTime endTime;
    private Long employeeId;
    private Long roomId;
    private Long workplaceId;
}
