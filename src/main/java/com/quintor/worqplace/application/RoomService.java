package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.RoomNotFoundException;
import com.quintor.worqplace.application.util.DateTimeUtils;
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

import static com.quintor.worqplace.application.util.DateTimeUtils.checkReservationDateTime;

@Service
@Transactional
public class RoomService {
	private final RoomRepository roomRepository;
	private final LocationService locationService;
	private ReservationService reservationService;

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

	public List<Room> getRoomsAvailableAtDateTime(Long locationId, LocalDate date, LocalTime startTime, LocalTime endTime) {
		checkReservationDateTime(date, startTime, endTime);

		List<Room> rooms = findRoomsByLocationId(locationId);

		return rooms
				.stream()
				.filter(room -> isRoomAvailable(room, date, startTime, endTime))
				.collect(Collectors.toList());
	}

	public List<Room> getRoomsWithWorkplacesAvailableAtDateTime(Long locationId, LocalDate date,
	                                                            LocalTime startTime, LocalTime endTime) {
		checkReservationDateTime(date, startTime, endTime);
		List<Room> allRooms = findRoomsByLocationId(locationId);

		return allRooms.stream().filter(room ->
				room.countReservedWorkspaces(date, startTime, endTime) < room.getCapacity())
				.collect(Collectors.toList());
	}

	public boolean isRoomAvailable(Room room, LocalDate date, LocalTime startTime, LocalTime endTime) {
		DateTimeUtils.checkReservationDateTime(date, startTime, endTime);
		return room.getReservations().stream().noneMatch((reservation -> DateTimeUtils.timeslotsOverlap(
				reservation.getDate(), reservation.getStartTime(),
				reservation.getEndTime(), reservation.getRecurrence(), date,
				startTime, endTime)));
	}

	public List<Room> findRoomsByLocationId(Long locationId) {
		Location location = locationService.getLocationById(locationId);
		return new ArrayList<>(location.getRooms());
	}

	/**
	 * For testing purposes
	 *
	 * @param reservationService reservationsService
	 */
	public void setReservationService(ReservationService reservationService) {
		this.reservationService = reservationService;
	}
}
