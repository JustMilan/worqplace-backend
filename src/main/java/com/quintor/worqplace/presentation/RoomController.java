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

@RestController
@RequestMapping("/rooms")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class RoomController {
	private final RoomService roomService;
	private final RoomMapper roomMapper;

	@GetMapping("/availability")
	public ResponseEntity<?> getRoomsAvailability(@RequestParam("locationId") Long locationId,
												  @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
												  @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
												  @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
		try {
			return new ResponseEntity<>(roomService.getRoomsAvailableAtDateTime(locationId, date, startTime, endTime)
					.stream().map(room -> roomMapper.toRoomDTO(room,
							room.getCapacity() - room.countReservedWorkspaces(date, startTime, endTime)))
					.collect(Collectors.toList()), HttpStatus.OK);
		} catch (InvalidDayException | InvalidStartAndEndTimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}

	@GetMapping("/availability/workplaces")
	public ResponseEntity<?> getWorkplacesAvailability(@RequestParam("locationId") Long locationId,
	                                              @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
	                                              @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
	                                              @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
		try {
			return new ResponseEntity<>(roomService.getRoomsWithWorkplacesAvailableAtDateTime(locationId, date, startTime, endTime)
					.stream().map(room -> roomMapper.toRoomDTO(room,
							room.getCapacity() - room.countReservedWorkspaces(date, startTime, endTime)))
					.collect(Collectors.toList()), HttpStatus.OK);
		} catch (InvalidDayException | InvalidStartAndEndTimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
