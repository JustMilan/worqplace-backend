package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.application.exceptions.RoomNotFoundException;
import com.quintor.worqplace.data.*;
import com.quintor.worqplace.domain.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class RoomServiceTest {
	private RoomRepository roomRepository;
	private LocationRepository locationRepository;

	private RoomService roomService;
	private ReservationService reservationService;

	private Room room;
	private Location location;


	@BeforeEach
	void initialize() {
		this.roomRepository = mock(RoomRepository.class);
		EmployeeRepository employeeRepository = mock(EmployeeRepository.class);
		this.locationRepository = mock(LocationRepository.class);
		ReservationRepository reservationRepository = mock(ReservationRepository.class);

		LocationService locationService = new LocationService(locationRepository);
		EmployeeService employeeService = new EmployeeService(employeeRepository);
		this.roomService = new RoomService(roomRepository, locationService);
		this.reservationService = new ReservationService(employeeService, roomService, reservationRepository);

		this.room = new Room(1L, 1, null, 15, Collections.emptyList());
		Address address = new Address(1L, 12, "", "QuintorStreet", "1454LJ", "QuintorCity");
		this.location = new Location(1L, "QuintorTest", address, List.of(room));

		Employee employee = new Employee(1L, "Firstname", "Lastname");

		setRepositoryBehaviour();
	}

	@Test
	void GetRoomByIdShouldReturnRoom() {
		assertEquals(room, roomService.getRoomById(room.getId()));
	}

	@Test
	void getRoomByIdShouldThrowIfNotFound() {
		assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(2L));
	}

	@Test
	void getAvailableRoomsForDateAndTimeShouldReturnAvailableRooms() {
		assertEquals(List.of(room), roomService.getRoomsAvailableAtDateTime(location.getId(), LocalDate.now().plusDays(4), LocalTime.of(5, 9), LocalTime.of(20, 8)));
	}

	@Test
	void getAvailableRoomsForDateAndTimeShouldThrowIfTimesAreInvalid() {
		assertThrows(InvalidStartAndEndTimeException.class, () -> roomService.getRoomsAvailableAtDateTime(location.getId(), LocalDate.now().plusDays(4), LocalTime.of(5, 9), LocalTime.of(5, 8)));
	}

	@Test
	void getAvailableRoomsForDateAndTimeShouldThrowIfDateIsInvalid() {
		assertThrows(InvalidDayException.class, () -> roomService.getRoomsAvailableAtDateTime(location.getId(), LocalDate.now().minusDays(4), LocalTime.of(5, 9), LocalTime.of(5, 10)));
	}

	@Test
	void isRoomAvailableShouldThrowIfTimeIsInvalid() {
		assertThrows(InvalidStartAndEndTimeException.class, () -> roomService.isRoomAvailable(room, LocalDate.now().plusDays(3), LocalTime.of(18, 1), LocalTime.of(16, 1)));
	}

	@Test
	void findRoomsByLocationIdShouldReturnRooms() {
		assertEquals(List.of(room), roomService.findRoomsByLocationId(1L));
	}

	@Test
	void getRoomsWithWorkplacesAvailableAtDateTimeShouldThrowOnInvalidTime() {
		assertThrows(InvalidDayException.class, () -> roomService.getRoomsWithWorkplacesAvailableAtDateTime(1L,
				LocalDate.now().minusDays(1), LocalTime.now(), LocalTime.now()));
	}

	@Test
	void getRoomsWithWorkplacesAvailableAtDateTimeShouldExecute() {
		assertDoesNotThrow(() -> roomService.getRoomsWithWorkplacesAvailableAtDateTime(1L,
				LocalDate.now().plusDays(1), LocalTime.MIDNIGHT, LocalTime.NOON));
	}

	private void setRepositoryBehaviour() {
		when(roomRepository.findById(room.getId())).thenReturn(java.util.Optional.ofNullable(room));
		when(roomRepository.findById(2L)).thenReturn(java.util.Optional.empty());

		when(locationRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(location));
	}
}
