package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.*;
import com.quintor.worqplace.data.*;
import com.quintor.worqplace.domain.*;
import com.quintor.worqplace.presentation.dto.reservation.ReservationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReservationServiceTest {
	private RoomService roomService;
	private LocationService locationService;
	private EmployeeService employeeService;
	private WorkplaceService workplaceService;
	private ReservationService reservationService;

	private RoomRepository roomRepository;
	private LocationRepository locationRepository;
	private EmployeeRepository employeeRepository;
	private WorkplaceRepository workplaceRepository;
	private ReservationRepository reservationRepository;

	private Room room;
	private Address address;
	private Employee employee;
	private Location location;
	private Workplace workplace;
	private Workplace workplace1;
	private Reservation reservation;
	private Reservation reservation1;
	private Reservation reservation2;
	private Reservation reservation3;

	@BeforeEach
	void initialize() {
		this.roomRepository = mock(RoomRepository.class);
		this.employeeRepository = mock(EmployeeRepository.class);
		this.locationRepository = mock(LocationRepository.class);
		this.workplaceRepository = mock(WorkplaceRepository.class);
		this.reservationRepository = mock(ReservationRepository.class);

		this.employeeService = new EmployeeService(employeeRepository);
		this.locationService = new LocationService(locationRepository);
		this.roomService = new RoomService(roomRepository, locationService, reservationService);
		this.workplaceService = new WorkplaceService(workplaceRepository, locationService, roomService);
		this.reservationService = new ReservationService(employeeService, workplaceService, roomService, reservationRepository);

		this.employee = new Employee(1L, "QFirstname", "QLastname");
		this.address = new Address(12, "", "TestStreet", "2098GS", "QuintorCity");

		this.room = new Room(1L, 1, null, null, Collections.emptyList());
		this.workplace = new Workplace(1L, room, null);
		this.workplace1 = new Workplace(2L, room, null);
		this.location = new Location(1L, "QuintorTest", address, List.of(room));
		this.reservation = new Reservation(1L, LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(15, 0), employee, null, workplace, false);
		this.reservation1 = new Reservation(2L, LocalDate.now().plusDays(4), LocalTime.of(12, 0), LocalTime.of(13, 0), employee, room, null, false);
		this.reservation2 = new Reservation(3L, LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(13, 0), employee, null, workplace, false);
		this.reservation3 = new Reservation(3L, LocalDate.now().plusMonths(3), LocalTime.of(9, 36), LocalTime.of(13, 10), employee, null, workplace, false);

		this.room.setLocation(location);
		room.setWorkplaces(List.of(workplace, workplace1));
		room.setReservations(List.of(reservation1));

		setRepositoryBehaviour();
	}

	@Test
	void getAllReservationsShouldReturnAllReservations() {
		assertEquals(List.of(reservation, reservation1), reservationService.getAllReservations());
	}

	@Test
	void getReservationByIdShouldReturnReservation() {
		assertEquals(reservation, reservationService.getReservationById(reservation.getId()));
	}

	@Test
	void getReservationByIdShouldThrowIfNotFound() {
		assertThrows(ReservationNotFoundException.class, () -> reservationService.getReservationById(3L));
	}

//	@Test TODO: FIX
//	void reserveWorkplaceShouldReserve() {
//		ReservationDTO reservationDTO = new ReservationDTO();
//		reservationDTO.setId(4L);
//		reservationDTO.setDate(reservation.getDate());
//		reservationDTO.setStartTime(reservation.getStartTime());
//		reservationDTO.setEndTime(reservation.getEndTime());
//		reservationDTO.setEmployeeId(employee.getId());
//		reservationDTO.setWorkplaceId(reservation.getWorkplace().getId());
//		reservationDTO.setRecurring(false);
//
//		assertEquals(reservation.getId(), reservationService.reserveWorkplace(reservationDTO).getId());
//	}

	@Test
	void reserveWorkplaceShouldThrowWhenRoomIdAndWorkplaceIdArePresent() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(5L);
		reservationDTO.setDate(reservation1.getDate());
		reservationDTO.setStartTime(reservation1.getStartTime());
		reservationDTO.setEndTime(reservation1.getEndTime());
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setWorkplaceId(2L);
		reservationDTO.setRoomId(reservation1.getRoom().getId());
		reservationDTO.setRecurring(false);

		assertThrows(InvalidReservationTypeException.class, () -> reservationService.reserveWorkplace(reservationDTO));
	}

	@Test
	void reserveWorkplaceShouldThrowWhenWorkplaceIsNull() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(5L);
		reservationDTO.setDate(reservation1.getDate());
		reservationDTO.setStartTime(reservation1.getStartTime());
		reservationDTO.setEndTime(reservation1.getEndTime());
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(reservation1.getRoom().getId());
		reservationDTO.setRecurring(false);

		assertThrows(NullPointerException.class, () -> reservationService.reserveWorkplace(reservationDTO));
	}

	@Test //TODO: FIX
	void reserveWorkplaceShouldThrowWhenWorkplaceIsNotAvailable() {
//		ReservationDTO reservationDTO = new ReservationDTO();
//		reservationDTO.setId(5L);
//		reservationDTO.setDate(reservation.getDate());
//		reservationDTO.setStartTime(reservation.getStartTime());
//		reservationDTO.setEndTime(reservation.getEndTime());
//		reservationDTO.setEmployeeId(employee.getId());
//		reservationDTO.setWorkplaceId(workplace.getId());
//		reservationDTO.setRecurring(false);
//
//		ReservationDTO reservationDTO1 = new ReservationDTO();
//		reservationDTO1.setId(6L);
//		reservationDTO1.setDate(reservation.getDate());
//		reservationDTO1.setStartTime(reservation.getStartTime());
//		reservationDTO1.setEndTime(reservation.getEndTime());
//		reservationDTO1.setEmployeeId(employee.getId());
//		reservationDTO1.setWorkplaceId(workplace.getId());
//		reservationDTO1.setRecurring(false);
//
//		reservationService.reserveWorkplace(reservationDTO);
//		assertThrows(WorkplaceNotAvailableException.class, () -> reservationService.reserveWorkplace(reservationDTO1));
	}

	@Test
	void reserveWorkplaceShouldThrowWhenEndTimeIsAfterBeginTime() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(5L);
		reservationDTO.setDate(reservation.getDate());
		reservationDTO.setStartTime(reservation.getEndTime());
		reservationDTO.setEndTime(reservation.getStartTime());
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setWorkplaceId(workplace.getId());
		reservationDTO.setRecurring(false);

		assertThrows(InvalidStartAndEndTimeException.class, () -> reservationService.reserveWorkplace(reservationDTO));

	}

	@Test //TODO: FIX
	void reserveWorkplaceShouldThrowWhenDateIsBeforeToday() {
//		ReservationDTO reservationDTO = new ReservationDTO();
//		reservationDTO.setId(8L);
//		reservationDTO.setDate(LocalDate.now().minusDays(5));
//		reservationDTO.setStartTime(LocalTime.of(8, 0));
//		reservationDTO.setEndTime(LocalTime.of(19, 3));
//		reservationDTO.setEmployeeId(employee.getId());
//		reservationDTO.setWorkplaceId(workplace.getId());
//		reservationDTO.setRecurring(false);
//
//		assertThrows(InvalidDayException.class, () -> reservationService.reserveWorkplace(reservationDTO));
	}

	@Test
	void toReservationShouldConvertCorrectly() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(8L);
		reservationDTO.setDate(reservation3.getDate());
		reservationDTO.setStartTime(reservation3.getStartTime());
		reservationDTO.setEndTime(reservation3.getEndTime());
		reservationDTO.setEmployeeId(reservation3.getEmployee().getId());
		reservationDTO.setWorkplaceId(reservation3.getWorkplace().getId());
		reservationDTO.setRecurring(false);

		assertEquals(reservation3.getDate(), reservationService.toReservation(reservationDTO).getDate());
		assertEquals(reservation3.getRoom(), reservationService.toReservation(reservationDTO).getRoom());
		assertEquals(reservation3.getEmployee(), reservationService.toReservation(reservationDTO).getEmployee());
		assertEquals(reservation3.getStartTime(), reservationService.toReservation(reservationDTO).getStartTime());
		assertEquals(reservation3.getEndTime(), reservationService.toReservation(reservationDTO).getEndTime());
		assertEquals(reservation3.getWorkplace(), reservationService.toReservation(reservationDTO).getWorkplace());
	}

	@Test
	void getReservationsForWorkplaceAtDateShouldReturnAvailableWorkplaces() {
		
	}

	@Test
	void getReservationsForWorkplaceAtDatesShouldReturnAvailableWorkplaces() {

	}

	@Test
	void isWorkplaceAvailableAtDateAndTimeShouldReturnAvailableWorkplaces() {

	}

	@Test
	void isWorkplaceAvailableAtDateShouldReturnAvailableWorkplaces() {

	}

	private void setRepositoryBehaviour() {
		when(reservationRepository.findById(3L)).thenReturn(Optional.empty());
		when(reservationRepository.save(reservation)).thenReturn(reservation);
		when(reservationRepository.findAll()).thenReturn(List.of(reservation, reservation1));
		when(reservationRepository.findById(reservation.getId())).thenReturn(java.util.Optional.ofNullable(reservation));

		when(employeeRepository.findById(1L)).thenReturn(Optional.ofNullable(employee));

		when(workplaceRepository.findById(workplace.getId())).thenReturn(Optional.ofNullable(workplace));
		when(workplaceRepository.findById(workplace1.getId())).thenReturn(Optional.ofNullable(workplace1));

		when(roomRepository.findById(room.getId())).thenReturn(Optional.ofNullable(room));

		when(locationRepository.findById(location.getId())).thenReturn(Optional.ofNullable(location));
	}
}
