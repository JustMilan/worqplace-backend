package com.quintor.worqplace.application.dto;

import com.quintor.worqplace.domain.Place;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class PlaceDTO {
    private final long id;
    private final String name;

    public PlaceDTO(Place place) {
        this.id = place.getId();
        this.name = place.getName();
    }
}
