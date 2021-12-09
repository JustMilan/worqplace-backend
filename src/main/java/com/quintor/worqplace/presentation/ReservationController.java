package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.ReservationService;
import com.quintor.worqplace.application.exceptions.*;
import com.quintor.worqplace.presentation.dto.reservation.ReservationDTO;
import com.quintor.worqplace.presentation.dto.reservation.ReservationMapper;
import lombok.AllArgsConstructor;
import org.springframework.context.annotation.Role;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.annotation.security.PermitAll;
import javax.annotation.security.RolesAllowed;
import javax.servlet.FilterChain;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.Base64;
import java.util.stream.Collectors;

import static java.util.Objects.requireNonNull;

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
	 * Fucntion that takes the subject from the jw token.
	 * The subject is the employeeId as set in de {@link com.quintor.worqplace.security.filter.JwtAuthenticationFilter#successfulAuthentication(HttpServletRequest, HttpServletResponse, FilterChain, Authentication)}
	 *
	 * @param token JW token with "Bearer " still in front of it.
	 * @return Employee ID of that is embedded in the token.
	 */
	private static long extractIdFromToken(String token) {
		//Strip the "Bearer "
		String[] bearToken = token.split(" ")[1].split("\\.");

		Base64.Decoder decoder = Base64.getDecoder();

		//header would be:  new String(decoder.decode(jwChunks[0]));
		String payload = new String(decoder.decode(bearToken[1]));

		String employeeId = payload.substring(payload.indexOf("sub\":") + 6).split("\"")[0];

		return Long.decode(employeeId);
	}

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
	 * @param id Id of the reservation.
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
			String jwt = ((ServletRequestAttributes) requireNonNull(RequestContextHolder.getRequestAttributes()))
					.getRequest().getHeader("Authorization");

			reservationDTO.setEmployeeId(extractIdFromToken(jwt));

			return new ResponseEntity<>(
					reservationMapper.toReservationDTO(reservationService.reserveWorkplaces(reservationDTO)),
					HttpStatus.CREATED
			);
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
			String jwt = ((ServletRequestAttributes) requireNonNull(RequestContextHolder.getRequestAttributes()))
					.getRequest().getHeader("Authorization");

			reservationDTO.setEmployeeId(extractIdFromToken(jwt));

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
	@GetMapping("/all")
	public ResponseEntity<?> getAllMyReservations() {
		String jwt = ((ServletRequestAttributes) requireNonNull(RequestContextHolder.getRequestAttributes()))
				.getRequest().getHeader("Authorization");

		return new ResponseEntity<>(reservationService.getAllMyReservations(extractIdFromToken(jwt))
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

	/**
	 * Function that deletes a singular reservation by calling to the {@link ReservationService} to delete
	 * {@link com.quintor.worqplace.domain.Reservation reservation} by id
	 *
	 * @return a ResponseEntity containing the id of the deleted
	 * {@link com.quintor.worqplace.domain.Reservation reservation}
	 */
	@PostMapping("/delete/{id}")
	public ResponseEntity<?> deleteById(@PathVariable long id) {
		reservationService.deleteReservation(id);
		return new ResponseEntity<>(id, HttpStatus.OK);
	}
}
