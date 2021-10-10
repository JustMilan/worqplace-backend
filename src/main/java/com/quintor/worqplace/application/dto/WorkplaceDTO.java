package com.quintor.worqplace.application.dto;

import com.quintor.worqplace.domain.Timeslot;
import com.quintor.worqplace.domain.Workplace;
import lombok.Getter;

import java.util.List;

@Getter
public class WorkplaceDTO {
    private final Long id;
    private final int workPlaceNumber;
    private final Long roomId;
    private final List<Timeslot> timeslots;

    public WorkplaceDTO(Workplace workplace) {
        this.id = workplace.getId();
        this.workPlaceNumber = workplace.getNumber();
        this.roomId = workplace.getRoom().getId();
        this.timeslots = workplace.getTimeslots();
    }
}
