package com.quintor.worqplace.application;

import com.quintor.worqplace.data.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.mockito.Mockito.mock;

class WorkplaceServiceTest {
	private RoomRepository roomRepository;
	private LocationRepository locationRepository;
	private EmployeeRepository employeeRepository;
	private WorkplaceRepository workplaceRepository;
	private ReservationRepository reservationRepository;

	private RoomService roomService;
	private EmployeeService employeeService;
	private LocationService locationService;
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
		this.roomService = new RoomService(roomRepository, locationService, reservationService);
		this.workplaceService = new WorkplaceService(workplaceRepository, locationService, reservationService);
		this.reservationService = new ReservationService(employeeService, workplaceService, roomService, reservationRepository);
	}

	@Test
	void getAllWorkplacesShouldReturnWorkplaces() {
	}

	@Test
	void getWorkplaceByIdShouldReturnWorkplace() {
	}

	@Test
	void getWorkplaceByIdShouldThrowIfNotFound() {
	}

	@Test
	void getWorkplacesAvailabilityShouldReturnAvailableWorkplaces() {
	}

	@Test
	void getWorkplacesAvailabilityShouldThrowIfTnvalidTime() {}

	@Test
	void getWorkplacesAvailabilityShouldThrowIfTnvalidDate() {}

	@Test
	void findWorkplacesByLocationIdShouldReturnLocations() {}

	@Test
	void isWorkplaceAvailableDuringDateAndTimeShouldReturnTrueIfItIsAvailable() {}

	@Test
	void isWorkplaceAvailableDuringDateAndTimeShouldReturnTrueIfThereAreNoReservations() {}
}
