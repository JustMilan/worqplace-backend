package com.quintor.worqplace.application;

import com.quintor.worqplace.application.exceptions.*;
import com.quintor.worqplace.data.EmployeeRepository;
import com.quintor.worqplace.data.LocationRepository;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.data.RoomRepository;
import com.quintor.worqplace.domain.*;
import com.quintor.worqplace.presentation.dto.reservation.ReservationDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

class ReservationServiceTest {
	private ReservationService reservationService;

	private RoomRepository roomRepository;
	private LocationRepository locationRepository;
	private EmployeeRepository employeeRepository;
	private ReservationRepository reservationRepository;

	private Room room;
	private Employee employee;
	private Location location;
	private Recurrence noRecurrence;
	private Reservation reservation;
	private Reservation reservation1;
	private Reservation reservation2;
	private Reservation reservation3;

	@BeforeEach
	void initialize() {
		this.roomRepository = mock(RoomRepository.class);
		this.employeeRepository = mock(EmployeeRepository.class);
		this.locationRepository = mock(LocationRepository.class);
		this.reservationRepository = mock(ReservationRepository.class);

		EmployeeService employeeService = new EmployeeService(employeeRepository);
		LocationService locationService = new LocationService(locationRepository);
		RoomService roomService = new RoomService(roomRepository, locationService, reservationService);
		this.reservationService = new ReservationService(employeeService, roomService, reservationRepository);

		roomService.setReservationService(reservationService);

		this.employee = new Employee(1L, "QFirstname", "QLastname");
		Address address = new Address(12, "", "TestStreet", "2098GS", "QuintorCity");

		this.room = new Room(1L, 1, null, 15, Collections.emptyList());
		this.location = new Location(1L, "QuintorTest", address, List.of(room));
		this.noRecurrence = new Recurrence(false, null);
		this.reservation = new Reservation(1L, LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(15, 0), employee, null, 15, noRecurrence);
		this.reservation1 = new Reservation(2L, LocalDate.now().plusDays(4), LocalTime.of(12, 0), LocalTime.of(13, 0), employee, room, 15, noRecurrence);
		this.reservation2 = new Reservation(3L, LocalDate.now().plusMonths(3), LocalTime.of(9, 36), LocalTime.of(13, 10), employee, room, 15, noRecurrence);
		this.reservation3 = new Reservation(4L, LocalDate.now().plusDays(1), LocalTime.of(18, 0), LocalTime.of(19, 0), employee, room, 15, noRecurrence);
		this.room.setLocation(location);
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

	@Test
	void reserveWorkplaceShouldThrowWhenWorkplaceIsNotAvailable() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(1L);
		reservationDTO.setDate(reservation.getDate());
		reservationDTO.setStartTime(reservation.getStartTime());
		reservationDTO.setEndTime(reservation.getEndTime());
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setWorkplaceAmount(15);
		reservationDTO.setRoomId(1L);
		reservationDTO.setRecurrence(this.noRecurrence);
		reservationService.reserveWorkplaces(reservationDTO);
		reservationDTO.setId(2L);

		assertThrows(WorkplacesNotAvailableException.class, () -> reservationService.reserveWorkplaces(reservationDTO));
	}

	@Test
	void reserveWorkplaceShouldThrowWhenEndTimeIsAfterBeginTime() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(5L);
		reservationDTO.setDate(reservation.getDate());
		reservationDTO.setStartTime(reservation.getEndTime());
		reservationDTO.setEndTime(reservation.getStartTime());
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(1L);
		reservationDTO.setWorkplaceAmount(15);
		reservationDTO.setRecurrence(this.noRecurrence);

		assertThrows(InvalidStartAndEndTimeException.class, () -> reservationService.reserveWorkplaces(reservationDTO));

	}

	@Test
	void reserveWorkplaceShouldThrowWhenDateIsBeforeToday() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(8L);
		reservationDTO.setDate(LocalDate.now().minusDays(5));
		reservationDTO.setStartTime(LocalTime.of(8, 0));
		reservationDTO.setEndTime(LocalTime.of(19, 3));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setWorkplaceAmount(15);
		reservationDTO.setRecurrence(this.noRecurrence);
		reservationDTO.setRoomId(1L);

		assertThrows(InvalidDayException.class, () -> reservationService.reserveWorkplaces(reservationDTO));
	}

	@Test
	void toReservationShouldConvertCorrectly() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(8L);
		reservationDTO.setDate(reservation2.getDate());
		reservationDTO.setStartTime(reservation2.getStartTime());
		reservationDTO.setEndTime(reservation2.getEndTime());
		reservationDTO.setEmployeeId(reservation2.getEmployee().getId());
		reservationDTO.setWorkplaceAmount(15);
		reservationDTO.setRoomId(1L);
		reservationDTO.setRecurrence(this.noRecurrence);

		assertEquals(reservation2.getDate(), reservationService.toReservation(reservationDTO).getDate());
		assertEquals(reservation2.getRoom(), reservationService.toReservation(reservationDTO).getRoom());
		assertEquals(reservation2.getEmployee(), reservationService.toReservation(reservationDTO).getEmployee());
		assertEquals(reservation2.getStartTime(), reservationService.toReservation(reservationDTO).getStartTime());
		assertEquals(reservation2.getEndTime(), reservationService.toReservation(reservationDTO).getEndTime());
		assertEquals(reservation2.getWorkplaceAmount(), reservationService.toReservation(reservationDTO).getWorkplaceAmount());
	}

	@Test
	void reserveRoomShouldThrowWhenRoomIsNull() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(8L);
		reservationDTO.setDate(reservation2.getDate());
		reservationDTO.setStartTime(reservation2.getStartTime());
		reservationDTO.setEndTime(reservation2.getEndTime());
		reservationDTO.setEmployeeId(reservation2.getEmployee().getId());
		reservationDTO.setRecurrence(this.noRecurrence);

		assertThrows(RoomNotFoundException.class, () -> reservationService.reserveRoom(reservationDTO));
	}

	@Test
	void reserveRoomShouldThrowIfItNotAvailable() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(8L);
		reservationDTO.setDate(reservation1.getDate());
		reservationDTO.setStartTime(reservation1.getStartTime());
		reservationDTO.setEndTime(reservation1.getEndTime());
		reservationDTO.setEmployeeId(reservation1.getEmployee().getId());
		reservationDTO.setRoomId(reservation1.getRoom().getId());
		reservationDTO.setRecurrence(this.noRecurrence);

		assertThrows(RoomNotAvailableException.class, () -> reservationService.reserveRoom(reservationDTO));
	}

	@Test
	void reserveRoomExecutesWhenAvailable() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setId(reservation3.getId());
		reservationDTO.setDate(reservation3.getDate());
		reservationDTO.setStartTime(reservation3.getStartTime());
		reservationDTO.setEndTime(reservation3.getEndTime());
		reservationDTO.setEmployeeId(reservation3.getEmployee().getId());
		reservationDTO.setRoomId(reservation3.getRoom().getId());
		reservationDTO.setRecurrence(reservation3.getRecurrence());
		assertDoesNotThrow(() -> reservationService.reserveRoom(reservationDTO));
	}

	@Test
	void getAllMyReservationsReturnsAllReservationsByUser() {
		assertEquals(3,
				reservationService.getAllMyReservations(this.employee.getId()).size());
	}

	private void setRepositoryBehaviour() {
		when(reservationRepository.findById(3L)).thenReturn(Optional.empty());
		when(reservationRepository.save(reservation)).thenReturn(reservation);
		when(reservationRepository.findAll()).thenReturn(List.of(reservation, reservation1));
		when(reservationRepository.findById(reservation.getId())).thenReturn(java.util.Optional.ofNullable(reservation));
		when(reservationRepository.findAllByEmployeeId(employee.getId()))
				.thenReturn(List.of(reservation, reservation1, reservation2));

		when(employeeRepository.findById(1L)).thenReturn(Optional.ofNullable(employee));

		when(roomRepository.findById(room.getId())).thenReturn(Optional.ofNullable(room));

		when(locationRepository.findById(location.getId())).thenReturn(Optional.ofNullable(location));
	}
}
