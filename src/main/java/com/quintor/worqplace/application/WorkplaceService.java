package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.application.exceptions.WorkplaceNotFoundException;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.data.WorkplaceRepository;
import com.quintor.worqplace.domain.Location;
import com.quintor.worqplace.domain.Reservation;
import com.quintor.worqplace.domain.Room;
import com.quintor.worqplace.domain.Workplace;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@Transactional
@AllArgsConstructor
public class WorkplaceService {
	private final ReservationRepository reservationRepository;
	private final WorkplaceRepository workplaceRepository;
	private final LocationService locationService;

	public List<Workplace> getAllWorkplaces() {
		return workplaceRepository.findAll();
	}

	public Workplace getWorkplaceById(Long id) {
		return workplaceRepository.findById(id)
				.orElseThrow(() -> new WorkplaceNotFoundException(id));
	}

	public List<Workplace> getWorkplacesAvailability(Long locationId, LocalDate date, LocalTime startTime, LocalTime endTime) {
		checkReservationDateTime(date, startTime, endTime);

		Map<Long, Map<LocalTime, LocalTime>> startAndEndTimesByWorkplace = new HashMap<>();
		List<Workplace> availableWorkplaces = findWorkplacesByLocationId(locationId);
		List<Reservation> reservations = reservationRepository.findAllByWorkplaceIsInAndWorkplaceIsNotNullAndDate(availableWorkplaces, date);

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

	private void checkReservationDateTime(LocalDate date, LocalTime startTime, LocalTime endTime) {
		if (startTime.isAfter(endTime))
			throw new InvalidStartAndEndTimeException();

		if (date.isBefore(LocalDate.now()))
			throw new InvalidDayException();
	}
}
