package com.quintor.worqplace.application;

import com.quintor.worqplace.application.dto.LocationDTO;
import com.quintor.worqplace.application.exceptions.LocationNotFoundException;
import com.quintor.worqplace.data.LocationRepository;
import com.quintor.worqplace.domain.Location;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class LocationSerivce {
    private final LocationRepository locationRepository;

    public List<LocationDTO> getAllLocations() {
        return locationRepository.findAll().stream().map(LocationDTO::new).collect(Collectors.toList());
    }

    public LocationDTO getLocationById(Long id) {
        Location location = locationRepository.findById(id).orElseThrow(
                () -> new LocationNotFoundException("Location " + id + " not found"));
        return new LocationDTO(location);
    }
}
