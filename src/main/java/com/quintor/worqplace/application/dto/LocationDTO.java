package com.quintor.worqplace.application.dto;

import com.quintor.worqplace.domain.Adress;
import com.quintor.worqplace.domain.Location;
import lombok.Getter;

@Getter
public class LocationDTO {
    private final Long id;
    private final String name;
    private final Adress adress;

    public LocationDTO(Location location) {
        this.id = location.getId();
        this.name = location.getName();
        this.adress = location.getAdress();
    }
}
