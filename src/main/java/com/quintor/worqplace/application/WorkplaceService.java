package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.application.exceptions.WorkplaceNotFoundException;
import com.quintor.worqplace.data.WorkplaceRepository;
import com.quintor.worqplace.domain.Location;
import com.quintor.worqplace.domain.Room;
import com.quintor.worqplace.domain.Workplace;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Transactional
public class WorkplaceService {
	private final WorkplaceRepository workplaceRepository;
	private final LocationService locationService;
	private final RoomService roomService;

	@Lazy
	public WorkplaceService(WorkplaceRepository workplaceRepository, LocationService locationService, RoomService roomService) {
		this.workplaceRepository = workplaceRepository;
		this.locationService = locationService;
		this.roomService = roomService;
	}

	public List<Workplace> getAllWorkplaces() {
		return workplaceRepository.findAll();
	}

	public Workplace getWorkplaceById(Long id) {
		return workplaceRepository
				.findById(id)
				.orElseThrow(() -> new WorkplaceNotFoundException(id));
	}

	public List<Workplace> getWorkplacesAvailability(Long locationId, LocalDate date, LocalTime startTime, LocalTime endTime) {
		if (startTime.isAfter(endTime))
			throw new InvalidStartAndEndTimeException();

		if (date.isBefore(LocalDate.now()))
			throw new InvalidDayException();

		List<Workplace> availableWorkplaces = findWorkplacesByLocationId(locationId);

		return availableWorkplaces.stream()
				.filter(workplace -> isWorkplaceAvailableDuringDateAndTime(workplace, date, startTime, endTime))
				.collect(Collectors.toList());
	}

	public List<Workplace> findWorkplacesByLocationId(Long locationId) {
		List<Workplace> workplacesByLocation = new ArrayList<>();

		Location location = locationService.getLocationById(locationId);

		for (Room room : location.getRooms())
			workplacesByLocation.addAll(room.getWorkplaces());

		return workplacesByLocation;
	}

	/**
	 * Function that iterates through workplaces to determine whether they are available during the provided timeslot.
	 *
	 * @param workplace The workplace that requires checking.
	 * @param date      The date of the timeslot.
	 * @param startTime The start time of the timeslot.
	 * @param endTime   The end time of the timeslot.
	 * @return True if the workplace
	 */
	public boolean isWorkplaceAvailableDuringDateAndTime(Workplace workplace, LocalDate date,
														 LocalTime startTime, LocalTime endTime) {
		return roomService.isRoomAvailable(workplace.getRoom(), date, startTime, endTime);
	}
}
