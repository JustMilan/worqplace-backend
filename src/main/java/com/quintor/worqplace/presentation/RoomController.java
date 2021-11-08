package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.RoomService;
import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.presentation.dto.room.RoomMapper;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

/**
 * Controller for {@link com.quintor.worqplace.domain.Room rooms}, contains
 * logic for getting all/specific
 * {@link com.quintor.worqplace.domain.Room rooms} and workplaces within them.
 *
 * @see RoomService
 * @see com.quintor.worqplace.domain.Room Room
 */
@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class RoomController {
	private final RoomService roomService;
	private final RoomMapper roomMapper;

	/**
	 * Function that calls to the {@link RoomService} to get the rooms that are fully
	 * available at a given location during a given timeslot.
	 *
	 * @param locationId id of the
	 *                   {@link com.quintor.worqplace.domain.Location Location}.
	 * @param date       date of which to get the availability.
	 * @param startTime  start time on the chosen date.
	 * @param endTime    end time on the chosen date.
	 * @return a ResponseEntity containing a
	 * {@link com.quintor.worqplace.presentation.dto.room.RoomDTO RoomDTO}.
	 */
	@GetMapping("/availability")
	public ResponseEntity<?> getRoomsAvailability(
			@RequestParam("locationId") Long locationId,
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
					LocalDate date,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
					LocalTime startTime,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
					LocalTime endTime) {
		try {
			return new ResponseEntity<>(roomService
					.getRoomsAvailableAtDateTime(locationId, date, startTime, endTime)
					.stream().map(room -> roomMapper.toRoomDTO(room, room.getCapacity()
							- room.countReservedWorkspaces(date, startTime, endTime)))
					.collect(Collectors.toList()), HttpStatus.OK);
		} catch (InvalidDayException | InvalidStartAndEndTimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	/**
	 * Function that calls to the {@link RoomService} to get the rooms that have
	 * at least one workplace available during the given timeslot.
	 *
	 * @param locationId id of the
	 *                   {@link com.quintor.worqplace.domain.Location Location}.
	 * @param date       date of which to get the availability.
	 * @param startTime  start time on the chosen date.
	 * @param endTime    end time on the chosen date.
	 * @return a ResponseEntity containing a
	 * {@link com.quintor.worqplace.presentation.dto.room.RoomDTO RoomDTO}.
	 */
	@GetMapping("/availability/workplaces")
	public ResponseEntity<?> getWorkplacesAvailability(
			@RequestParam("locationId") Long locationId,
			@RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
					LocalDate date,
			@RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
					LocalTime startTime,
			@RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME)
					LocalTime endTime) {
		try {
			return new ResponseEntity<>(roomService
					.getRoomsWithWorkplacesAvailableAtDateTime(locationId, date,
							startTime, endTime)
					.stream().map(room -> roomMapper.toRoomDTO(room,
							room.getCapacity() - room
									.countReservedWorkspaces(date, startTime, endTime)))
					.collect(Collectors.toList()), HttpStatus.OK);
		} catch (InvalidDayException | InvalidStartAndEndTimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
