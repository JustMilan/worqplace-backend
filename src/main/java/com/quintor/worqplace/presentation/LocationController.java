package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.LocationSerivce;
import com.quintor.worqplace.application.exceptions.LocationNotFoundException;
import com.quintor.worqplace.application.exceptions.ReservationNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/locations")
@AllArgsConstructor
public class LocationController {
    private final LocationSerivce locationSerivce;

    @GetMapping
    public ResponseEntity<?> getAllLocations() {
        return new ResponseEntity<>(locationSerivce.getAllLocations(), HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(locationSerivce.getLocationById(id), HttpStatus.OK);
        } catch (LocationNotFoundException locationNotFoundException) {
            return new ResponseEntity<>(locationNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
