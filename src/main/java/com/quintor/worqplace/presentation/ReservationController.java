package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.ReservationService;
import com.quintor.worqplace.application.exceptions.*;
import com.quintor.worqplace.presentation.dto.reservation.ReservationDTO;
import com.quintor.worqplace.presentation.dto.reservation.ReservationMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import java.util.stream.Collectors;

/**
 * Controller for {@link com.quintor.worqplace.domain.Reservation reservations}, contains
 * logic for getting all/specific reservations and reserving
 * {@link com.quintor.worqplace.domain.Room rooms} and workplaces within them.
 *
 * @see ReservationService
 * @see com.quintor.worqplace.domain.Reservation Reservation
 * @see com.quintor.worqplace.domain.Room Room
 */
@RestController
@RequestMapping("/reservations")
@AllArgsConstructor
public class ReservationController {
	private final ReservationService reservationService;
	private final ReservationMapper reservationMapper;

	/**
	 * Function that calls to the {@link ReservationService} to get all
	 * {@link com.quintor.worqplace.domain.Reservation reservations} and then maps
	 * them to a list of
	 * {@link com.quintor.worqplace.presentation.dto.reservation.ReservationDTO
	 * ReservationDTOs}.
	 *
	 * @return a ResponseEntity containing a list of
	 * {@link com.quintor.worqplace.presentation.dto.reservation.ReservationDTO ReservationDTOs}.
	 */
	@GetMapping
	public ResponseEntity<?> getAllReservations() {
		return new ResponseEntity<>(reservationService.getAllReservations().stream().map(reservationMapper::toReservationDTO)
				.collect(Collectors.toList()), HttpStatus.OK);
	}

	/**
	 * Function that calls to the {@link ReservationService} to get a
	 * {@link com.quintor.worqplace.domain.Reservation reservation} and then maps
	 * it to a {@link com.quintor.worqplace.presentation.dto.reservation.ReservationDTO
	 * ReservationDTO}.
	 *
	 * @return a ResponseEntity containing a {@link ReservationDTO}.
	 * @see ReservationService
	 * @see com.quintor.worqplace.domain.Reservation Reservation
	 */
	@GetMapping("/{id}")
	public ResponseEntity<?> getReservationById(@PathVariable long id) {
		try {
			return new ResponseEntity<>(reservationMapper
					.toReservationDTO(reservationService.getReservationById(id)),
					HttpStatus.OK);
		} catch (ReservationNotFoundException reservationNotFoundException) {
			return new ResponseEntity<>(reservationNotFoundException.getMessage(),
					HttpStatus.NOT_FOUND);
		}
	}

	/**
	 * Function that calls to the {@link ReservationService} to create a
	 * {@link com.quintor.worqplace.domain.Reservation Reservation} for a specific
	 * amount of workplaces in the selected {@link com.quintor.worqplace.domain.Room
	 * room} and then maps the return value to a {@link ReservationDTO}.
	 *
	 * @param reservationDTO DTO containing information about the reservation.
	 * @return a ResponseEntity containing a {@link ReservationDTO}.
	 * @see ReservationService
	 * @see com.quintor.worqplace.domain.Reservation Reservation
	 */
	@PostMapping("/workplaces")
	public ResponseEntity<?> reserveWorkplaces(@RequestBody ReservationDTO reservationDTO) {
		try {
			return new ResponseEntity<>(reservationMapper
					.toReservationDTO(reservationService
							.reserveWorkplaces(reservationDTO)), HttpStatus.CREATED);
		} catch (WorkplacesNotAvailableException | RoomNotAvailableException | InvalidStartAndEndTimeException |
				InvalidDayException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Function that calls to the {@link ReservationService} to create a
	 * {@link com.quintor.worqplace.domain.Reservation Reservation} for a
	 * {@link com.quintor.worqplace.domain.Room room} and then maps the
	 * return value to a {@link ReservationDTO}.
	 *
	 * @param reservationDTO DTO containing information about the reservation.
	 * @return a ResponseEntity containing a {@link ReservationDTO}.
	 * @see ReservationService
	 * @see com.quintor.worqplace.domain.Reservation Reservation
	 * @see com.quintor.worqplace.domain.Room Room
	 */
	@PostMapping("/rooms")
	public ResponseEntity<?> reserveRoom(@RequestBody ReservationDTO reservationDTO) {
		try {
			return new ResponseEntity<>(reservationMapper
					.toReservationDTO(reservationService.reserveRoom(reservationDTO)),
					HttpStatus.CREATED);
		} catch (RoomNotAvailableException | WorkplacesNotAvailableException |
				InvalidStartAndEndTimeException | InvalidDayException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Function that calls to the {@link ReservationService} to get all
	 * {@link com.quintor.worqplace.domain.Reservation reservations} by a user and then
	 * maps them to a list of
	 * {@link com.quintor.worqplace.presentation.dto.reservation.ReservationDTO
	 * ReservationDTOs}.
	 *
	 * @return a ResponseEntity containing a list of
	 * {@link com.quintor.worqplace.presentation.dto.reservation.ReservationDTO
	 * ReservationDTOs}.
	 */
	@GetMapping("/{id}/all")
	public ResponseEntity<?> getAllMyReservations(@PathVariable long id) {
		return new ResponseEntity<>(reservationService.getAllMyReservations(id)
				.stream().map(reservationMapper::toReservationDTO)
				.collect(Collectors.toList()), HttpStatus.OK);
	}

	@RolesAllowed("ROLE_ADMIN")
	@GetMapping("/location/{id}")
	public ResponseEntity<?> getAllByLocation(@PathVariable long id) {
		return new ResponseEntity<>(reservationService.getAllByLocation(id)
				.stream().map(reservationMapper::toReservationDTO)
				.collect(Collectors.toList()), HttpStatus.OK);
	}
}
