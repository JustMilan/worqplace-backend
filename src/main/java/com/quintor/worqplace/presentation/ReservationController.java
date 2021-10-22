package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.ReservationService;
import com.quintor.worqplace.application.exceptions.WorkplaceNotAvailableException;
import com.quintor.worqplace.presentation.dto.reservation.ReservationDTO;
import com.quintor.worqplace.presentation.dto.reservation.ReservationMapper;
import com.quintor.worqplace.application.exceptions.InvalidReservationTypeException;
import com.quintor.worqplace.application.exceptions.ReservationNotFoundException;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.stream.Collectors;

@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
    private final ReservationService reservationService;
    private final ReservationMapper reservationMapper;

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping
    public ResponseEntity<?> getAllReservations() {
        return new ResponseEntity<>(reservationService.getAllReservations().stream().map(reservationMapper::toReservationDTO)
                .collect(Collectors.toList()), HttpStatus.OK);
    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @GetMapping("/{id}")
    public ResponseEntity<?> getReservationById(@PathVariable long id) {
        try {
            return new ResponseEntity<>(reservationMapper.toReservationDTO(reservationService.getReservationById(id)), HttpStatus.OK);
        } catch (ReservationNotFoundException reservationNotFoundException) {
            return new ResponseEntity<>(reservationNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
        }
    }

    @CrossOrigin(origins = {"http://localhost:4200"})
    @PostMapping("/workplaces")
    public ResponseEntity<?> reserveWorkplace(@RequestBody ReservationDTO reservationDTO) {
        try {
            return new ResponseEntity<>(reservationMapper.toReservationDTO(reservationService.reserveWorkplace(reservationDTO)), HttpStatus.CREATED);
        } catch (InvalidReservationTypeException | WorkplaceNotAvailableException e) {
            return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
        }
    }
}
