package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.application.exceptions.RoomNotFoundException;
import com.quintor.worqplace.data.EmployeeRepository;
import com.quintor.worqplace.data.LocationRepository;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.data.RoomRepository;
import com.quintor.worqplace.domain.Address;
import com.quintor.worqplace.domain.Employee;
import com.quintor.worqplace.domain.Location;
import com.quintor.worqplace.domain.Room;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
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
	@DisplayName("getRoomById() should return a room if it exists")
	void getRoomByIdShouldReturnRoom() {
		assertEquals(room, roomService.getRoomById(room.getId()));
	}

	@Test
	@DisplayName("getRoomById() should throw RoomNotFoundException if it hasn't been found")
	void getRoomByIdShouldThrowIfNotFound() {
		assertThrows(RoomNotFoundException.class, () -> roomService.getRoomById(2L));
	}

	@Test
	@DisplayName("getAvailableRoomsForDateAndTime() should return available rooms")
	void getAvailableRoomsForDateAndTimeShouldReturnAvailableRooms() {
		assertEquals(List.of(room), roomService.getRoomsAvailableAtDateTime(location.getId(), LocalDate.now().plusDays(4), LocalTime.of(5, 9), LocalTime.of(20, 8)));
	}

	@Test
	@DisplayName("getAvailableRoomsForDateAndTime() should throw InvalidStartAndEndTimeException if times are invalid")
	void getAvailableRoomsForDateAndTimeShouldThrowIfTimesAreInvalid() {
		assertThrows(InvalidStartAndEndTimeException.class, () -> roomService.getRoomsAvailableAtDateTime(location.getId(), LocalDate.now().plusDays(4), LocalTime.of(5, 9), LocalTime.of(5, 8)));
	}

	@Test
	@DisplayName("getAvailableRoomsForDateAndTime() should throw if date is invalid")
	void getAvailableRoomsForDateAndTimeShouldThrowIfDateIsInvalid() {
		assertThrows(InvalidDayException.class, () -> roomService.getRoomsAvailableAtDateTime(location.getId(), LocalDate.now().minusDays(4), LocalTime.of(5, 9), LocalTime.of(5, 10)));
	}

	@Test
	@DisplayName("isRoomAvailable() should throw InvalidStartAndEndTimeException if time is invalid")
	void isRoomAvailableShouldThrowIfTimeIsInvalid() {
		assertThrows(InvalidStartAndEndTimeException.class, () -> roomService.isRoomAvailable(room, LocalDate.now().plusDays(3), LocalTime.of(18, 1), LocalTime.of(16, 1)));
	}

	@Test
	@DisplayName("findRoomsByLocationId() should return rooms if there are any")
	void findRoomsByLocationIdShouldReturnRooms() {
		assertEquals(List.of(room), roomService.findRoomsByLocationId(1L));
	}

	@Test
	@DisplayName("getRoomsWithWorkplacesAvailableAtDateTime() should throw InvalidDayException if time is invalid")
	void getRoomsWithWorkplacesAvailableAtDateTimeShouldThrowOnInvalidTime() {
		assertThrows(InvalidDayException.class, () -> roomService.getRoomsWithWorkplacesAvailableAtDateTime(1L, LocalDate.now().minusDays(1), LocalTime.now(), LocalTime.now()));
	}

	@Test
	@DisplayName("getRoomsWithWorkplacesAvailableAtDateTime() should not throw if parameter data is correct")
	void getRoomsWithWorkplacesAvailableAtDateTimeShouldExecute() {
		assertDoesNotThrow(() -> roomService.getRoomsWithWorkplacesAvailableAtDateTime(1L,
				LocalDate.now().plusDays(1), LocalTime.MIDNIGHT, LocalTime.NOON));
	}

	/**
	 * Sets mock behaviour for repositories used in this test file
	 */
	private void setRepositoryBehaviour() {
//		Room repository
		when(roomRepository.findById(room.getId())).thenReturn(java.util.Optional.ofNullable(room));
		when(roomRepository.findById(2L)).thenReturn(java.util.Optional.empty());

//		Location repository
		when(locationRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(location));
	}
}
