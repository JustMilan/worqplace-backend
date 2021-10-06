package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.LocationSerivce;
import com.quintor.worqplace.application.exceptions.LocationNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/locations")
@AllArgsConstructor
public class LocationController {
    private final LocationSerivce locationSerivce;

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping
    public ResponseEntity<?> getAllLocations() {
        return new ResponseEntity<>(locationSerivce.getAllLocations(), HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getLocationById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(locationSerivce.getLocationById(id), HttpStatus.OK);
        } catch (LocationNotFoundException locationNotFoundException) {
            return new ResponseEntity<>(locationNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }
}
