package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.application.exceptions.RoomNotFoundException;
import com.quintor.worqplace.data.RoomRepository;
import com.quintor.worqplace.domain.Location;
import com.quintor.worqplace.domain.Room;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import static com.quintor.worqplace.application.util.DateTimeUtils.checkReservationDate;
import static com.quintor.worqplace.application.util.DateTimeUtils.checkReservationDateTime;

@Service
@Transactional
public class RoomService {
	private final RoomRepository roomRepository;
	private final LocationService locationService;
	private final ReservationService reservationService;

	@Lazy
	public RoomService(RoomRepository roomRepository, LocationService locationService, ReservationService reservationService) {
		this.roomRepository = roomRepository;
		this.locationService = locationService;
		this.reservationService = reservationService;
	}

	public Room getRoomById(Long id) {
		return roomRepository.findById(id)
				.orElseThrow(() -> new RoomNotFoundException(id));
	}

	public List<Room> getRoomsAvailabilityForDate(Long locationId, LocalDate date) {
		if (! checkReservationDate(date))
			throw new InvalidStartAndEndTimeException();

		List<Room> rooms = findRoomsByLocationId(locationId);

		return rooms
				.stream()
				.filter(room -> isRoomAvailable(locationId, room, date))
				.collect(Collectors.toList());
	}

	public List<Room> getRoomsAvailabilityForDateAndTime(Long locationId, LocalDate date, LocalTime startTime, LocalTime endTime) {
		checkReservationDateTime(date, startTime, endTime);
		List<Room> rooms = findRoomsByLocationId(locationId);

		return rooms
				.stream()
				.filter(room -> isRoomAvailable(locationId, room, date, startTime, endTime))
				.collect(Collectors.toList());
	}

	public boolean isRoomAvailable(Long locationId, Room room, LocalDate date) {
		return room.getWorkplaces()
				.stream()
				.allMatch(workplace -> reservationService.isWorkplaceAvailableAt(workplace, date));
	}

	public boolean isRoomAvailable(Long locationId, Room room, LocalDate date, LocalTime startTime, LocalTime endTime) {
		return room.getWorkplaces()
				.stream()
				.allMatch(workplace -> reservationService.isWorkplaceAvailableAt(workplace, date, startTime, endTime));
	}

	public List<Room> findRoomsByLocationId(Long locationId) {
		Location location = locationService.getLocationById(locationId);
		return new ArrayList<>(location.getRooms());
	}
}
