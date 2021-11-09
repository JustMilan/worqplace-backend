package com.quintor.worqplace.presentation;

import com.quintor.worqplace.CiTestConfiguration;
import com.quintor.worqplace.data.ReservationRepository;
import com.quintor.worqplace.domain.*;
import com.quintor.worqplace.presentation.dto.reservation.ReservationDTO;
import com.quintor.worqplace.presentation.dto.reservation.ReservationMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("ci")
@Import(CiTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class ReservationControllerIntegrationTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Autowired
	private ReservationRepository reservationRepository;

	@Autowired
	private ReservationMapper reservationMapper;

	private Employee employee;
	private Employee employee1;

	private Room room;
	private Room room1;

	private Address address;
	private Address address1;

	private Location location;
	private Location location1;

	private Recurrence recurrence;
	private Recurrence recurrence1;

	private Reservation reservation;
	private Reservation reservation1;
	private Reservation reservation2;
	private Reservation reservation3;
	private Reservation reservation4;
	private Reservation reservation5;

	@BeforeAll
	void initialize() {
//		Employee
		this.employee = new Employee(1L, "Quinten", "Tor");
		this.employee1 = new Employee(2L, "Quintara", "Tor");

//		Address
		this.address = new Address(1L, 1, "", "Torro", "4369GH", "Torr");
		this.address1 = new Address(2L, 12, "A", "Zuidel straat", "1249LJ", "Quintara");

//		Location
		this.location = new Location(1L, "Quintor - Test", address, null);
		this.location1 = new Location(2L, "Quintor - Zuid", address1, null);


//		Room
		this.room = new Room(1L, 1, location, 5, null);
		this.room1 = new Room(2L, 2, location, 8, null);

//		Recurrence
		this.recurrence = new Recurrence(true, RecurrencePattern.MONTHLY);
		this.recurrence1 = new Recurrence(false, RecurrencePattern.WEEKLY);

//		Workplace
		this.reservation = new Reservation(1L, LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(19, 0), employee, room, 1, recurrence);
		reservationRepository.save(reservation);

//		this.reservation1 = new Reservation(2L, LocalDate.now().plusWeeks(1), LocalTime.of(9, 0), LocalTime.of(19, 0), employee, room, 1, recurrence);
//		this.reservation2 = new Reservation(3L, , , , , , );

//		Room
		this.reservation3 = new Reservation(4L, LocalDate.now().plusWeeks(2), LocalTime.of(7, 3), LocalTime.of(8, 7), employee1, room1, 8, recurrence1);
//		this.reservation4 = new Reservation(5L, , , , , , );
//		this.reservation5 = new Reservation(6L, , , , , , );
	}

	@Test
	@Order(1)
	@DisplayName("getAllReservations() should return 200 OK")
	void shouldReturn200() {
		assertEquals(HttpStatus.OK, this.restTemplate.getForEntity(String.format("http://localhost:%s/reservations/", port), String.class).getStatusCode());
	}

	@Test
	@DisplayName("getAllReservations() should return all reservations")
	void shouldReturnAllReservations() {

	}

	@Test
	@Order(2)
	@DisplayName("getAllReservations should return an empty list of reservations if there are none")
	void shouldReturnEmptyListIfNoReservations() {
		assertEquals(Collections.emptyList().toString(), this.restTemplate.getForObject(String.format("http://localhost:%s/reservations/", port), String.class));
	}

	@Test
	@DisplayName("getReservationById() should return reservation 200 OK there is one")
	void getReservationByIdShouldReturn200() {
		assertEquals(HttpStatus.OK, this.restTemplate.getForEntity(String.format("http://localhost:%s/reservations/%s", port, "1"), String.class).getStatusCode());
	}

	@Test
	@DisplayName("getReservationById() should return reservation if there is one")
	void getReservationByIdShouldReturnReservation() {
		String result = this.restTemplate.getForEntity(String.format("http://localhost:%s/reservations/", port), String.class).getBody();
		assertTrue(
			result.contains("\"startTime\":\"09:00:00\",\"endTime\":\"19:00:00\",\"employeeId\":1,\"roomId\":1,\"workplaceAmount\":1,\"recurrence\":{\"active\":true,\"recurrencePattern\":\"MONTHLY\"}")
		);
	}

	@Test
	@DisplayName("getReservationById() should return 404 when not found")
	void getReservationByIdShouldReturn404() {
		assertEquals(HttpStatus.NOT_FOUND, this.restTemplate.getForEntity(String.format("http://localhost:%s/reservations/%s", port, "999"), String.class).getStatusCode());
	}

	@Test
	@DisplayName("reserveWorkplaces() should return 201 upon reservation")
	void reserveWorkplacesShouldReturn201() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().plusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(19, 0));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setWorkplaceAmount(1);
		reservationDTO.setRecurrence(recurrence1);

		assertEquals(HttpStatus.CREATED, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), reservationDTO, String.class).getStatusCode());

	}

	@Test
	@DisplayName("reserveWorkplaces() should return reservation info if reservation went successful")
	void reserveWorkplacesShouldReturnReservationInfo() {
	}

	@Test
	@DisplayName("reserveWorkplaces() should return 422 if workplace is not available")
	void reserveWorkplaceShouldReturn422IfNotAvailable() {
	}

	@Test
	@DisplayName("reserveWorkplaces() should return 422 if times are invalid")
	void reserveWorkplaceShouldReturn422IfTimesAreInvalid() {
	}

	@Test
	@DisplayName("reserveWorkplaces() should return 422 if date is invalid")
	void reserveWorkplaceShouldReturn422IfDateIsInvalid() {
	}

	@Test
	@DisplayName("reserveRoom() should return 201 upon reservation")
	void reserveRoomShouldReturn201() {
	}

	@Test
	@DisplayName("reserveRoom() should return reservation info if reservation went successful")
	void reserveRoomShouldReturnReservationInfo() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().plusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(8, 59));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setWorkplaceAmount(1);
		reservationDTO.setRecurrence(recurrence);

		assertNotNull(this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), reservationDTO, String.class).getBody());
	}

	@Test
	@DisplayName("reserveRoom() should return 422 if room is not available")
	void reserveRoomeShouldReturn422IfNotAvailable() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().plusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(8, 59));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setWorkplaceAmount(1);
		reservationDTO.setRecurrence(recurrence);

		this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), reservationDTO, String.class);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), reservationDTO, String.class).getStatusCode());
	}

	@Test
	@DisplayName("reserveRoom() should return 422 if times are invalid")
	void reserveRoomShouldReturn422IfTimesAreInvalid() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().plusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(8, 59));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setWorkplaceAmount(1);
		reservationDTO.setRecurrence(recurrence);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), reservationDTO, String.class).getStatusCode());
	}

	@Test
	@DisplayName("reserveRoom() should return 422 if date is invalid")
	void reserveRoomShouldReturn422IfDateIsInvalid() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().minusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(19, 0));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setWorkplaceAmount(1);
		reservationDTO.setRecurrence(recurrence);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), reservationDTO, String.class).getStatusCode());
	}

	@Test
	@DisplayName("getAllMyReservations() should return 200 OK")
	void getAllMyReservationsShouldReturn200() {
		assertEquals(HttpStatus.OK, this.restTemplate.getForEntity(String.format("http://localhost:%s/reservations/1/all", port), String.class).getStatusCode());
	}

	@Test
	@DisplayName("getAllMyReservations() should return reservations if there are any")
	void getAllMyReservationsShouldReturnListOfReservations() {
	}

	@Test
	@Order(3)
	@DisplayName("getAllMyReservations() should return empty list if there are none")
	void getAllMyReservationsShouldReturnEmptyList() {
		assertEquals(Collections.emptyList().toString(), this.restTemplate.getForObject(String.format("http://localhost:%s/reservations/1/%s", port, "all"), String.class));
	}
}
