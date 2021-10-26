package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.LocationService;
import com.quintor.worqplace.presentation.dto.location.LocationMapper;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/locations")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class LocationController {
	private final LocationService locationService;
	private final LocationMapper locationMapper;

	@GetMapping
	private ResponseEntity<?> getAllLocations() {
		return new ResponseEntity<>(locationService.getAllLocations().stream().map(locationMapper::toLocationDTO)
				.collect(Collectors.toList()), HttpStatus.OK);
	}
}
