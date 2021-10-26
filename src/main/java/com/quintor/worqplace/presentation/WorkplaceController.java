package com.quintor.worqplace.presentation;

import com.quintor.worqplace.application.RoomService;
import com.quintor.worqplace.application.WorkplaceService;
import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.application.exceptions.WorkplaceNotFoundException;
import com.quintor.worqplace.presentation.dto.room.RoomMapper;
import com.quintor.worqplace.presentation.dto.workplace.WorkplaceMapper;
import lombok.AllArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.stream.Collectors;

@RestController
@RequestMapping("workplaces")
@AllArgsConstructor
@CrossOrigin(origins = {"http://localhost:4200"})
public class WorkplaceController {
	private final WorkplaceService workplaceService;
	private final WorkplaceMapper workplaceMapper;

	@GetMapping
	public ResponseEntity<?> getAllWorkplaces() {
		return new ResponseEntity<>(workplaceService.getAllWorkplaces().stream().map(workplaceMapper::toWorkplaceDTO).collect(Collectors.toList()), HttpStatus.OK);
	}

	@GetMapping("/{id}")
	public ResponseEntity<?> getWorkplaceById(@PathVariable long id) {
		try {
			return new ResponseEntity<>(workplaceMapper.toWorkplaceDTO(workplaceService.getWorkplaceById(id)), HttpStatus.OK);
		} catch (WorkplaceNotFoundException workplaceNotFoundException) {
			return new ResponseEntity<>(workplaceNotFoundException.getMessage(), HttpStatus.NOT_FOUND);
		}
	}

	@GetMapping("/availability")
	public ResponseEntity<?> getWorkplacesAvailability(@RequestParam("locationId") Long locationId,
													   @RequestParam("date") @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
													   @RequestParam("start") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime startTime,
													   @RequestParam("end") @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalTime endTime) {
		try {
			return new ResponseEntity<>(workplaceService.getWorkplacesAvailability(locationId, date, startTime, endTime)
					.stream().map(workplaceMapper::toWorkplaceDTO).collect(Collectors.toList()), HttpStatus.OK);
		} catch (InvalidDayException | InvalidStartAndEndTimeException e) {
			return new ResponseEntity<>(e.getMessage(), HttpStatus.UNPROCESSABLE_ENTITY);
		}
	}
}
