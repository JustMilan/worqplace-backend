package com.quintor.worqplace.application.dto;

import com.quintor.worqplace.domain.Workplace;
import lombok.Getter;

import java.util.List;
import java.util.stream.Collectors;

@Getter
public class WorkplaceReservationDTO {
    private final boolean successful;
    private final Long id;
    private final int workPlaceNumber;
    private final List<TimeslotAvailibilityDTO> timeslots;

    public WorkplaceReservationDTO(Workplace workplace, boolean successful) {
        this.successful = successful;
        this.id = workplace.getId();
        this.workPlaceNumber = workplace.getNumber();
        this.timeslots = workplace.getTimeslots().stream().map((TimeslotAvailibilityDTO::new)).collect(Collectors.toList());
    }
}
