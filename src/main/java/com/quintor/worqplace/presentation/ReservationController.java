package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.ReservationService;
import com.quintor.worqplace.application.exceptions.ReservationNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping
    public ResponseEntity<?> getAllReservations() {
        return new ResponseEntity<>(reservationService.getAllReservations(), HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationbyId(@PathVariable long id) {
        try {
            return new ResponseEntity<>(reservationService.getReservationById(id), HttpStatus.OK);
        } catch (ReservationNotFoundException reservationNotFoundException) {
            return new ResponseEntity<>(reservationNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @PutMapping("/workplaces/{id}")
    public ResponseEntity<?> placeReservationByWorkplaceId(@PathVariable long id) {    //TODO: Add employee id to parameters
        return new ResponseEntity<>(reservationService.placeReservationByWorkplaceId(id), HttpStatus.OK);
    }
}
