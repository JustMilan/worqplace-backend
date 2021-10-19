package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.LocationNotFoundException;
import com.quintor.worqplace.data.LocationRepository;
import com.quintor.worqplace.domain.Location;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.List;

@Service
@Transactional
@AllArgsConstructor
public class LocationService {
    private final LocationRepository locationRepository;

    public List<Location> getAllLocations() {
        return locationRepository.findAll();
    }

    public Location getLocationById(Long id) {
        return locationRepository.findById(id).orElseThrow(
                () -> new LocationNotFoundException(id));
    }
}