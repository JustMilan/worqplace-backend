package com.quintor.worqplace.application;

import com.quintor.worqplace.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

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
	}

	@Test
	void GetRoomByIdShouldReturnRoom() {
	}

	@Test
	void getRoomByIdShouldThrowIfNotFound() {
	}

	@Test
	void getRoomsAvailabilityForDateShouldReturnAvailableRooms() {
	}

	@Test
	void getRoomsAvailabilityForDateShouldThrowIfDateIsInvalid() {
	}

	@Test
	void getAvailableRoomsForDateAndTimeShouldReturnAvailableRooms() {
	}

	@Test
	void getAvailableRoomsForDateAndTimeShouldThrowIfTimesAreInvalid() {
	}

	@Test
	void getAvailableRoomsForDateAndTimeShouldThrowIfDateIsInvalid() {
	}

	@Test
	void isRoomAvailableShouldReturnTrueIfRoomIsAvailable() {
	}

	@Test
	void isRoomAvailableShouldReturnFalseIfRoomIsNotAvailable() {
	}

	@Test
	void isRoomAvailableShouldThrowIfDateIsInvalid() {
	}

	@Test
	void isRoomAvailableShouldThrowIfTimeIsInvalid() {
	}

	@Test
	void findRoomsByLocationIdShouldReturnRooms() {
	}
}
