package com.quintor.worqplace.application.dto;

import com.quintor.worqplace.domain.Workplace;
import lombok.Getter;

@Getter
public class WorkplaceDTO {
    private final Long id;

    public WorkplaceDTO(Workplace workplace) {
        this.id = workplace.getId();
    }
}