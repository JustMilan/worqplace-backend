package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.WorkplaceNotFoundException;
import com.quintor.worqplace.data.WorkplaceRepository;
import com.quintor.worqplace.domain.Location;
import com.quintor.worqplace.domain.Reservation;
import com.quintor.worqplace.domain.Room;
import com.quintor.worqplace.domain.Workplace;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.*;
import java.util.stream.Collectors;

import static com.quintor.worqplace.application.util.DateTimeUtils.checkReservationDateTime;

@Service
@Transactional
public class WorkplaceService {
	private final WorkplaceRepository workplaceRepository;
	private final LocationService locationService;
	private final ReservationService reservationService;

	@Lazy
	public WorkplaceService(WorkplaceRepository workplaceRepository, LocationService locationService, ReservationService reservationService) {
		this.workplaceRepository = workplaceRepository;
		this.locationService = locationService;
		this.reservationService = reservationService;
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
		checkReservationDateTime(date, startTime, endTime);

		Map<Long, Map<LocalTime, LocalTime>> startAndEndTimesByWorkplace = new HashMap<>();
		List<Workplace> availableWorkplaces = findWorkplacesByLocationId(locationId);
		List<Reservation> reservations = reservationService.findAllByWorkplacesAndDate(availableWorkplaces, date);

		reservations.forEach(reservation -> {
			Long key = reservation.getWorkplace().getId();
			Map<LocalTime, LocalTime> innerMap = new HashMap<>();

			if (! startAndEndTimesByWorkplace.containsKey(key)) {
				innerMap.put(reservation.getStartTime(), reservation.getEndTime());
				startAndEndTimesByWorkplace.put(reservation.getWorkplace().getId(), innerMap);
			} else {
				innerMap = startAndEndTimesByWorkplace.get(key);
				innerMap.put(reservation.getStartTime(), reservation.getEndTime());
			}
		});

		startAndEndTimesByWorkplace.forEach((key, value) ->
				value.forEach((reservationStartTime, reservationEndTime) -> {
					// to be reserved time and reserved times are overlapping
					if (reservationStartTime.isBefore(endTime) && startTime.isBefore(reservationEndTime))
						availableWorkplaces.remove(getWorkplaceById(key));
				}));

		// only return unique workplaces
		return availableWorkplaces.stream().distinct().collect(Collectors.toList());
	}

	public List<Workplace> findWorkplacesByLocationId(Long locationId) {
		List<Workplace> workplacesByLocation = new ArrayList<>();
		Location location = locationService.getLocationById(locationId);

		for (Room room : location.getRooms())
			workplacesByLocation.addAll(room.getWorkplaces());

		return workplacesByLocation;
	}

	public List<Workplace> findWorkplacesByLocationAndRoomId(Long locationId, Long roomId) {
		List<Workplace> workplacesByLocation = findWorkplacesByLocationId(locationId);
		return workplacesByLocation
				.stream()
				.filter(workplace -> Objects.equals(workplace.getRoom().getId(), roomId))
				.collect(Collectors.toList());
	}

	public List<Reservation> checkWorkplaceAvailabilityForDay(Workplace workplace, LocalDate date) {
		return reservationService.getReservationsForWorkplaceAtDate(workplace.getId(), date);
	}

	public List<Reservation> getWorkplaceAvailability(Long locationId, Long workplaceId, LocalDate date) {
		List<Workplace> workplacesByLocation = findWorkplacesByLocationId(locationId);
		Workplace workplace = null;

		for (Workplace w : workplacesByLocation)
			if (w.getId().equals(workplaceId))
				workplace = w;

		if (workplace == null)
			throw new WorkplaceNotFoundException(workplaceId);

		return reservationService.getReservationsForWorkplaceAtDate(workplaceId, date);
	}
}
