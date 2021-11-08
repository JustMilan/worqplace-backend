package com.quintor.worqplace.presentation;

import com.quintor.worqplace.CiTestConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("ci")
@Import(CiTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class ReservationControllerIntegrationTest {
	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@BeforeEach
	void initialize() {

	}

	@Test
	@DisplayName("getAllReservations() should return 200 OK")
	void shouldReturn200() {

	}

	@Test
	@DisplayName("getAllReservations() should return all reservations")
	void shouldReturnAllReservations() {

	}

	@Test
	@DisplayName("getAllReservations should return an empty list of reservations if there are none")
	void shouldReturnEmptyListIfNoReservations() {

	}

	@Test
	@DisplayName("getReservationById() should return reservation 200 OK there is one")
	void getReservationByIdShouldReturn200() {

	}

	@Test
	@DisplayName("getReservationById() should return reservation if there is one")
	void getReservationByIdShouldReturnReservation() {

	}

	@Test
	@DisplayName("getReservationById() should return 404 when not found")
	void getReservationByIdShouldReturn404() {
	}

	@Test
	@DisplayName("reserveWorkplaces() should return 201 upon reservation")
	void reserveWorkplacesShouldReturn201() {
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
	}

	@Test
	@DisplayName("reserveRoom() should return 422 if room is not available")
	void reserveRoomeShouldReturn422IfNotAvailable() {
	}

	@Test
	@DisplayName("reserveRoom() should return 422 if times are invalid")
	void reserveRoomShouldReturn422IfTimesAreInvalid() {

	}

	@Test
	@DisplayName("reserveRoom() should return 422 if date is invalid")
	void reserveRoomShouldReturn422IfDateIsInvalid() {
	}

	@Test
	@DisplayName("getAllMyReservations() should return 200 OK")
	void getAllMyReservationsShouldReturn200() {
	}

	@Test
	@DisplayName("getAllMyReservations() should return reservations if there are any")
	void getAllMyReservationsShouldReturnListOfReservations() {
	}

	@Test
	@DisplayName("getAllMyReservations() should return empty list if there are none")
	void getAllMyReservationsShouldReturnEmptyList() {
	}
}
