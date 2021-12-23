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
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.net.URI;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import static java.util.Objects.requireNonNull;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("ci")
@Import(CiTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
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

	private Recurrence recurrence;
	private Recurrence recurrence1;

	private Reservation reservation;
	private Reservation reservation1;
	private Reservation reservation3;

	private String bearer;

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

//		Room
		this.room = new Room(1L, 1, location, 5, null);
		this.room1 = new Room(2L, 2, location, 8, null);

//		Recurrence
		this.recurrence = new Recurrence(true, RecurrencePattern.MONTHLY);
		this.recurrence1 = new Recurrence(false, RecurrencePattern.WEEKLY);

//		Workplace
		this.reservation = new Reservation(1L, LocalDate.now().plusDays(1), LocalTime.of(9, 0), LocalTime.of(19, 0), employee, room, 1, recurrence);
		this.reservation1 = new Reservation(2L, LocalDate.now().plusWeeks(1), LocalTime.of(9, 0), LocalTime.of(19, 0), employee, room, 2, recurrence);

//		Room
		this.reservation3 = new Reservation(4L, LocalDate.now().plusWeeks(2), LocalTime.of(7, 3), LocalTime.of(8, 7), employee1, room1, 8, recurrence1);

		setupBearerToken();
		createNewEmployee();
	}

	@AfterEach
	void tearDown() {
		reservationRepository.deleteAll();
	}

	@Test
	@DisplayName("getAllReservations() should return 200 OK")
	void shouldReturn200() {
		ResponseEntity<String> result = getRequest("/reservations");
		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	@DisplayName("getAllReservations() should return all reservations")
	void shouldReturnAllReservations() {
		reservationRepository.save(reservation1);
		reservationRepository.save(reservation);

		ResponseEntity<String> result = getRequest("/reservations");

		assertTrue(requireNonNull(result.getBody()).contains("\"startTime\":\"09:00:00\",\"endTime\":\"19:00:00\",\"employeeId\":1,\"roomId\":1,\"workplaceAmount\":1,\"recurrence\":{\"active\":true,\"recurrencePattern\":\"MONTHLY\"}") &&
				requireNonNull(result.getBody()).contains("\"startTime\":\"09:00:00\",\"endTime\":\"19:00:00\",\"employeeId\":1,\"roomId\":1,\"workplaceAmount\":2,\"recurrence\":{\"active\":true,\"recurrencePattern\":\"MONTHLY\"}"));
	}

	@Test
	@DisplayName("getAllReservations should return an empty list of reservations if there are none")
	void shouldReturnEmptyListIfNoReservations() {
		ResponseEntity<String> result = getRequest("/reservations");

		assertEquals(Collections.emptyList().toString(), result.getBody());
	}

	@Test
	@DisplayName("getReservationById() should return reservation 200 OK there is one")
	void getReservationByIdShouldReturn200() {
		reservationRepository.save(reservation);
		ResponseEntity<String> result = getRequest("/reservations/1");

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	@DisplayName("getReservationById() should return reservation if there is one")
	void getReservationByIdShouldReturnReservation() {
		reservationRepository.save(reservation);
		ResponseEntity<String> result = getRequest("/reservations");

		assertTrue(
				requireNonNull(result.getBody()).contains("\"startTime\":\"09:00:00\",\"endTime\":\"19:00:00\",\"employeeId\":1,\"roomId\":1,\"workplaceAmount\":1,\"recurrence\":{\"active\":true,\"recurrencePattern\":\"MONTHLY\"}")
		);
	}

	@Test
	@DisplayName("getReservationById() should return 404 when not found")
	void getReservationByIdShouldReturn404() {
		ResponseEntity<String> result = getRequest("/reservations/99");

		assertEquals(HttpStatus.NOT_FOUND, result.getStatusCode());
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
		reservationDTO.setWorkplaceAmount(4);
		reservationDTO.setRecurrence(recurrence1);

		var headers = new HttpHeaders();
		headers.set("Authorization", this.bearer);
		var request = new HttpEntity<>(reservationDTO, headers);

		assertEquals(HttpStatus.CREATED, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/workplaces", port), request, String.class).getStatusCode());
	}

	@Test
	@DisplayName("reserveWorkplaces() should return reservation info if reservation went successful")
	void reserveWorkplacesShouldReturnReservationInfo() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().plusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(19, 0));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setWorkplaceAmount(1);
		reservationDTO.setRecurrence(recurrence1);

		var headers = new HttpHeaders();
		headers.set("Authorization", this.bearer);
		var request = new HttpEntity<>(reservationDTO, headers);

		String result = this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/workplaces", port), request, String.class).getBody();

		assertTrue(requireNonNull(result).contains(String.format(",\"date\":\"%s\",\"startTime\":\"09:00:00\",\"endTime\":\"19:00:00\",\"employeeId\":1,\"roomId\":1,\"workplaceAmount\":1,\"recurrence\":{\"active\":false,\"recurrencePattern\":\"NONE\"}}", reservationDTO.getDate())));
	}

	@Test
	@DisplayName("reserveWorkplaces() should return 422 if workplace is not available")
	void reserveWorkplaceShouldReturn422IfNotAvailable() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().plusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(19, 0));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setWorkplaceAmount(13);
		reservationDTO.setRecurrence(recurrence1);

		reservationRepository.save(reservation3);

		var headers = new HttpHeaders();
		headers.set("Authorization", this.bearer);
		var request = new HttpEntity<>(reservationDTO, headers);

		String result = this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/workplaces", port), request, String.class).getBody();

		assertTrue(requireNonNull(result).contains(String.format(",\"date\":\"%s\",\"startTime\":\"09:00:00\",\"endTime\":\"19:00:00\",\"employeeId\":1,\"roomId\":1,\"workplaceAmount\":13,\"recurrence\":{\"active\":false,\"recurrencePattern\":\"NONE\"}}", reservationDTO.getDate())));
	}

	@Test
	@DisplayName("reserveWorkplaces() should return 422 if times are invalid")
	void reserveWorkplaceShouldReturn422IfTimesAreInvalid() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().plusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(8, 57));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setWorkplaceAmount(4);
		reservationDTO.setRecurrence(recurrence1);

		var headers = new HttpHeaders();
		headers.set("Authorization", this.bearer);
		var request = new HttpEntity<>(reservationDTO, headers);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/workplaces", port), request, String.class).getStatusCode());
	}

	@Test
	@DisplayName("reserveWorkplaces() should return 422 if date is invalid")
	void reserveWorkplaceShouldReturn422IfDateIsInvalid() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().minusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(19, 0));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setWorkplaceAmount(1);
		reservationDTO.setRecurrence(recurrence1);

		reservationRepository.save(reservation3);

		var headers = new HttpHeaders();
		headers.set("Authorization", this.bearer);
		var request = new HttpEntity<>(reservationDTO, headers);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/workplaces", port), request, String.class).getStatusCode());
	}

	@Test
	@DisplayName("reserveRoom() should return 201 upon reservation")
	void reserveRoomShouldReturn201() {
		ReservationDTO reservationDTO = new ReservationDTO();
		reservationDTO.setDate(LocalDate.now().plusDays(1));
		reservationDTO.setStartTime(LocalTime.of(9, 0));
		reservationDTO.setEndTime(LocalTime.of(19, 0));
		reservationDTO.setEmployeeId(employee.getId());
		reservationDTO.setRoomId(room.getId());
		reservationDTO.setRecurrence(recurrence1);

		var headers = new HttpHeaders();
		headers.set("Authorization", this.bearer);
		var request = new HttpEntity<>(reservationDTO, headers);

		assertEquals(HttpStatus.CREATED, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), request, String.class).getStatusCode());
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

		var headers = new HttpHeaders();
		headers.set("Authorization", this.bearer);
		var request = new HttpEntity<>(reservationDTO, headers);

		this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), request, String.class);
		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), request, String.class).getStatusCode());
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

		var headers = new HttpHeaders();
		headers.set("Authorization", this.bearer);
		var request = new HttpEntity<>(reservationDTO, headers);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), request, String.class).getStatusCode());
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

		var headers = new HttpHeaders();
		headers.set("Authorization", this.bearer);
		var request = new HttpEntity<>(reservationDTO, headers);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, this.restTemplate.postForEntity(String.format("http://localhost:%s/reservations/rooms", port), request, String.class, headers).getStatusCode());
	}

	@Test
	@DisplayName("getAllMyReservations() should return 200 OK")
	void getAllMyReservationsShouldReturn200() {
		var result = getRequest("/reservations/all");

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	@DisplayName("getAllMyReservations() should return reservations if there are any")
	void getAllMyReservationsShouldReturnListOfReservations() {
		reservationRepository.save(reservation);
		reservationRepository.save(reservation1);

		var result = getRequest("/reservations/all");

		assertTrue(result.getBody().contains(String.format("\"date\":\"%s\",\"startTime\":\"09:00:00\",\"endTime\":\"19:00:00\",\"employeeId\":1,\"roomId\":1,\"workplaceAmount\":1,\"recurrence\":{\"active\":true,\"recurrencePattern\":\"MONTHLY\"}},", reservation.getDate())) &&
				result.getBody().contains(String.format("\"date\":\"%s\",\"startTime\":\"09:00:00\",\"endTime\":\"19:00:00\",\"employeeId\":1,\"roomId\":1,\"workplaceAmount\":2,\"recurrence\":{\"active\":true,\"recurrencePattern\":\"MONTHLY\"}}]", reservation1.getDate())));
	}

	@Test
	@DisplayName("getAllMyReservations() should return empty list if there are none")
	void getAllMyReservationsShouldReturnEmptyList() {
		var result = getRequest("/reservations/all");

		assertEquals(Collections.emptyList().toString(), result.getBody());
	}

	@Test
	@DisplayName("deleteReservation() should return 200 OK")
	void deleteOwnReservation() {
		reservationRepository.save(reservation);
		var result = getRequest("/reservations/");
		Matcher m = Pattern.compile("id\":(.*?),").matcher(result.getBody());
		boolean found = m.find();

		assertTrue(found);

		result = postRequest("/reservations/delete/" + m.group(1));

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	@DisplayName("deleteReservation() should return 403 when not own")
	void deleteOtherReservation() {
		reservationRepository.save(reservation3);
		var result = getRequest("/reservations/");
		Matcher m = Pattern.compile("id\":(.*?),").matcher(result.getBody());
		boolean found = m.find();

		assertTrue(found);

		result = postRequest("/reservations/delete/" + m.group(1));

		assertEquals(HttpStatus.FORBIDDEN, result.getStatusCode());
	}

	/**
	 * Function that uses the {@link TestRestTemplate} to send a GET request
	 * to the Back-End for testing during Continuous Integration.
	 *
	 * @param url the URL Path after "https://localhost:8080".
	 *            as a {@link String}
	 * @return a {@link ResponseEntity} for the request.
	 */
	ResponseEntity<String> getRequest(String url) {
		var request = RequestEntity.get(URI.create(url))
				.header("Authorization", this.bearer)
				.build();

		return restTemplate.exchange(request, String.class);
	}

	/**
	 * Function that uses the {@link TestRestTemplate} to send a POST request
	 * to the Back-End for testing during Continuous Integration.
	 *
	 * @param url the URL Path after "https://localhost:8080".
	 *            as a {@link String}
	 * @return a {@link ResponseEntity} for the request.
	 */
	ResponseEntity<String> postRequest(String url) {
		var request = RequestEntity.post(URI.create(url))
				.header("Authorization", this.bearer)
				.build();

		return restTemplate.exchange(request, String.class);
	}

	/**
	 * Function that uses the {@link TestRestTemplate} to send a DELETE request
	 * to the Back-End for testing during Continuous Integration.
	 *
	 * @param url the URL Path after "https://localhost:8080".
	 *            as a {@link String}
	 * @return a {@link ResponseEntity} for the request.
	 */
	ResponseEntity<String> deleteRequest(String url) {
		var request = RequestEntity.delete(URI.create(url))
				.header("Authorization", this.bearer)
				.build();

		return restTemplate.exchange(request, String.class);
	}

	private void setupBearerToken() {
		try {
			Map<String, String> map1 = new HashMap<>();
			map1.put("username", "mdol@quintor.nl");
			map1.put("password", "Kaasje");

			this.bearer = (restTemplate.postForEntity("http://localhost:" + port + "/login", map1, String.class))
					.getHeaders().get("Authorization")
					.toString()
					.replace("[", "")
					.replace("]", "");
		} catch (Exception e) {
			String url = "http://localhost:" + port + "/register";

			Map<String, String> map = new HashMap<>();
			map.put("firstname", "Milan");
			map.put("lastname", "Dol");
			map.put("username", "mdol@quintor.nl");
			map.put("password", "Kaasje");

			restTemplate.postForEntity(url, map, Void.class);

			Map<String, String> map1 = new HashMap<>();
			map1.put("username", "mdol@quintor.nl");
			map1.put("password", "Kaasje");

			this.bearer = (restTemplate.postForEntity("http://localhost:" + port + "/login", map1, String.class))
					.getHeaders().get("Authorization")
					.toString()
					.replace("[", "")
					.replace("]", "");
		}
	}

	private void createNewEmployee() {
		String url = "http://localhost:" + port + "/register";

		Map<String, String> map = new HashMap<>();
		map.put("firstname", "Nieuwe");
		map.put("lastname", "Employee");
		map.put("username", "nemp@quintor.nl");
		map.put("password", "Biertje");

		restTemplate.postForEntity(url, map, Void.class);
	}
}
