package com.quintor.worqplace.application.dto.location;

import com.quintor.worqplace.domain.Address;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LocationDTO {
    private Long id;
    private String name;
    private Address address;
}
