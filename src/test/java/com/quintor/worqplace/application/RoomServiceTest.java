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
	private EmployeeRepository employeeRepository;
	private WorkplaceRepository workplaceRepository;
	private ReservationRepository reservationRepository;

	private RoomService roomService;
	private LocationService locationService;
	private EmployeeService employeeService;
	private WorkplaceService workplaceService;
	private ReservationService reservationService;

	private Room room;
	private Address address;
	private Location location;
	private Employee employee;
	private Workplace workplace;
	private Workplace workplace1;


	@BeforeEach
	void initialize() {
		this.roomRepository = mock(RoomRepository.class);
		this.employeeRepository = mock(EmployeeRepository.class);
		this.locationRepository = mock(LocationRepository.class);
		this.workplaceRepository = mock(WorkplaceRepository.class);
		this.reservationRepository = mock(ReservationRepository.class);

		this.locationService = new LocationService(locationRepository);
		this.employeeService = new EmployeeService(employeeRepository);
		this.workplaceService = new WorkplaceService(workplaceRepository, locationService, roomService);
		this.roomService = new RoomService(roomRepository, locationService, reservationService);
		this.reservationService = new ReservationService(employeeService, workplaceService, roomService, reservationRepository);
		this.roomService.setReservationService(reservationService);

		this.room = new Room(1L, 1, null, null, Collections.emptyList());
		this.workplace = new Workplace(1L, room, Collections.emptyList());
		this.workplace1 = new Workplace(2L, room, Collections.emptyList());
		this.address = new Address(1L, 12, "", "QuintorStreet", "1454LJ", "QuintorCity");
		this.location = new Location(1L, "QuintorTest", address, List.of(room));

		this.room.setWorkplaces(List.of(workplace, workplace1));
		this.employee = new Employee(1L, "Firstname", "Lastname");

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
		assertEquals(List.of(room), roomService.getAvailableRoomsForDateAndTime(location.getId(), LocalDate.now().plusDays(4), LocalTime.of(5, 9), LocalTime.of(20, 8)));
	}

	@Test
	void getAvailableRoomsForDateAndTimeShouldThrowIfTimesAreInvalid() {
		assertThrows(InvalidStartAndEndTimeException.class, () -> roomService.getAvailableRoomsForDateAndTime(location.getId(), LocalDate.now().plusDays(4), LocalTime.of(5, 9), LocalTime.of(5, 8)));
	}

	@Test
	void getAvailableRoomsForDateAndTimeShouldThrowIfDateIsInvalid() {
		assertThrows(InvalidDayException.class, () -> roomService.getAvailableRoomsForDateAndTime(location.getId(), LocalDate.now().minusDays(4), LocalTime.of(5, 9), LocalTime.of(5, 10)));
	}

	@Test
	void isRoomAvailableShouldThrowIfTimeIsInvalid() {
		assertThrows(InvalidStartAndEndTimeException.class, () -> roomService.isRoomAvailable(room, LocalDate.now().plusDays(3), LocalTime.of(18, 1), LocalTime.of(16, 1), false));
	}

	@Test
	void findRoomsByLocationIdShouldReturnRooms() {
		assertEquals(List.of(room), roomService.findRoomsByLocationId(1L));
	}

	private void setRepositoryBehaviour() {
		when(roomRepository.findById(room.getId())).thenReturn(java.util.Optional.ofNullable(room));
		when(roomRepository.findById(2L)).thenReturn(java.util.Optional.empty());

		when(locationRepository.findById(1L)).thenReturn(java.util.Optional.ofNullable(location));
	}
}
