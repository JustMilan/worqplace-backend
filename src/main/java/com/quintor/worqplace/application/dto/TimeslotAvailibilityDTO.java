package com.quintor.worqplace.application.dto;

import com.quintor.worqplace.domain.Timeslot;
import com.quintor.worqplace.domain.Workplace;
import lombok.Getter;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.List;

@Getter
public class TimeslotAvailibilityDTO {
    private final Long id;
    private final LocalDate date;
    private final LocalTime startTime;
    private final LocalTime endTime;
    private final boolean hasReservation;

    public TimeslotAvailibilityDTO(Timeslot timeslot) {
        this.id = timeslot.getId();
        this.date = timeslot.getDate();
        this.startTime = timeslot.getStartTime();
        this.endTime = timeslot.getEndTime();
        this.hasReservation = timeslot.hasReservation();
    }
}
