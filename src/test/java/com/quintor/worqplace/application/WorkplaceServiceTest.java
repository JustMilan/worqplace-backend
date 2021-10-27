package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.InvalidDayException;
import com.quintor.worqplace.application.exceptions.InvalidStartAndEndTimeException;
import com.quintor.worqplace.application.exceptions.WorkplaceNotFoundException;
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
		this.roomService = new RoomService(roomRepository, locationService, reservationService);
		this.workplaceService = new WorkplaceService(workplaceRepository, locationService, roomService);
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
	void getAllWorkplacesShouldReturnWorkplaces() {
		assertEquals(List.of(workplace, workplace1), workplaceService.getAllWorkplaces());
	}

	@Test
	void getWorkplaceByIdShouldReturnWorkplace() {
		assertEquals(workplace, workplaceService.getWorkplaceById(workplace.getId()));
	}

	@Test
	void getWorkplaceByIdShouldThrowIfNotFound() {
		assertThrows(WorkplaceNotFoundException.class, () -> workplaceService.getWorkplaceById(workplace1.getId()));
	}

	@Test
	void getWorkplacesAvailabilityShouldReturnAvailableWorkplaces() {
		assertEquals(List.of(workplace, workplace1), workplaceService.getWorkplacesAvailability(location.getId(), LocalDate.now().plusDays(65), LocalTime.of(3, 1), LocalTime.of(18, 37)));
	}

	@Test
	void getWorkplacesAvailabilityShouldThrowIfInvalidTime() {
		assertThrows(InvalidStartAndEndTimeException.class, () -> workplaceService.getWorkplacesAvailability(location.getId(), LocalDate.now().plusDays(1), LocalTime.of(14, 0), LocalTime.of(13, 0)));
	}

	@Test
	void getWorkplacesAvailabilityShouldThrowIfInvalidDate() {
		assertThrows(InvalidDayException.class, () -> workplaceService.getWorkplacesAvailability(location.getId(), LocalDate.now().minusDays(1), LocalTime.of(9, 0), LocalTime.of(13, 0)));
	}

	@Test
	void findWorkplacesByLocationIdShouldReturnLocations() {
		assertEquals(List.of(workplace, workplace1), workplaceService.findWorkplacesByLocationId(location.getId()));
	}

	@Test
	void isWorkplaceAvailableDuringDateAndTimeShouldReturnTrueIfItIsAvailable() {
		assertTrue(() -> workplaceService.isWorkplaceAvailableDuringDateAndTime(workplace, LocalDate.now().plusDays(2), LocalTime.of(9, 5), LocalTime.of(16, 5)));
	}

	@Test
	void isWorkplaceAvailableDuringDateAndTimeShouldReturnTrueIfThereAreNoReservations() {
		LocalDate localDate = LocalDate.now().plusDays(2);
		LocalTime startTime = LocalTime.of(9, 33);
		LocalTime endTime = LocalTime.of(14, 54);
		Reservation reservation = new Reservation(1L, localDate, startTime, endTime, employee, null, workplace, false);

		when(this.reservationRepository.findAll()).thenReturn(List.of(reservation));

		assertFalse(() -> workplaceService.isWorkplaceAvailableDuringDateAndTime(workplace, localDate, startTime, endTime));
	}

	@Test
	void isWorkplaceAvailableDuringDateAndTimeShouldReturnFalseWhenNotAvailable() {
		LocalDate localDate = LocalDate.now().plusDays(12);
		LocalTime startTime = LocalTime.of(9, 2);
		LocalTime endTime = LocalTime.of(9, 3);

		Reservation reservation = new Reservation(1L, localDate, startTime, endTime, employee, room, null, false);

		when(reservationRepository.findAll()).thenReturn(List.of(reservation));

		assertFalse(() -> workplaceService.isWorkplaceAvailableDuringDateAndTime(workplace, localDate, startTime, endTime));
	}

	private void setRepositoryBehaviour() {
		when(this.workplaceRepository.findAll()).thenReturn(List.of(workplace, workplace1));
		when(this.workplaceRepository.findById(workplace.getId())).thenReturn(java.util.Optional.ofNullable(workplace));
		when(this.workplaceRepository.findById(workplace1.getId())).thenReturn(java.util.Optional.empty());

		when(this.locationRepository.findById(location.getId())).thenReturn(java.util.Optional.ofNullable(location));

		when(this.reservationRepository.findAll()).thenReturn(Collections.emptyList());
	}
}
