package com.quintor.worqplace.presentation;

import com.quintor.worqplace.CiTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.assertEquals;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("ci")
@Import(CiTestConfiguration.class)
public class RoomControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	@Test
	@DisplayName("Test if a date in far FAR in the future has any rooms available.")
	void listOfAvailableRoomsNotEmpty() {
		String urlPart = "/rooms/availability?";
		urlPart += "locationId=5&";
		urlPart += "date=6000-01-01&";
		urlPart += "start=14:00&";
		urlPart += "end=15:00";

		ResponseEntity<String> result = getRequest(urlPart);

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	@DisplayName("listOfAvailableRoomsNotEmpty() should not return an empty list")
	void listOfAvailableRoomsContent() {
		String urlPart = "/rooms/availability?";
		urlPart += "locationId=5&";
		urlPart += "date=6000-01-01&";
		urlPart += "start=14:00&";
		urlPart += "end=15:00";

		ResponseEntity<String> result = getRequest(urlPart);

		assertEquals(result.getBody(), "[{\"id\":1,\"floor\":3,\"capacity\":24,\"available\":24},{\"id\":2,\"floor\":-1,\"capacity\":6,\"available\":6}]");
	}

	@Test
	@DisplayName("Test if a checking room availability in the past returns an error.")
	void pastRoomsReturnsError() {
		String urlPart = "/rooms/availability?";
		urlPart += "locationId=5&";
		urlPart += "date=2010-01-01&";
		urlPart += "start=14:00&";
		urlPart += "end=15:00";

		ResponseEntity<String> result = getRequest(urlPart);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, result.getStatusCode());
	}

	@Test
	@DisplayName("Test if a date in far FAR in the future has any workplaces available.")
	void listOfAvailableWorkplacessNotEmpty() {
		String urlPart = "/rooms/availability/workplaces?";
		urlPart += "locationId=5&";
		urlPart += "date=6000-01-01&";
		urlPart += "start=14:00&";
		urlPart += "end=15:00";

		ResponseEntity<String> result = getRequest(urlPart);

		assertEquals(HttpStatus.OK, result.getStatusCode());
	}

	@Test
	@DisplayName("getWorkplacesAvailability() should not return an empty list")
	void getWorkplacesAvailabilityShouldNotBeEmpty() {
		String urlPart = "/rooms/availability/workplaces?";
		urlPart += "locationId=5&";
		urlPart += "date=6000-01-01&";
		urlPart += "start=14:00&";
		urlPart += "end=15:00";

		ResponseEntity<String> result = getRequest(urlPart);

		assertEquals(result.getBody(), "[{\"id\":1,\"floor\":3,\"capacity\":24,\"available\":24},{\"id\":2,\"floor\":-1,\"capacity\":6,\"available\":6}]");
	}

	@Test
	@DisplayName("Test if a checking workplace availability in the past returns an error.")
	void pastWorkplacesReturnsError() {
		String urlPart = "/rooms/availability/workplaces?";
		urlPart += "locationId=5&";
		urlPart += "date=2010-01-01&";
		urlPart += "start=14:00&";
		urlPart += "end=15:00";

		ResponseEntity<String> result = getRequest(urlPart);

		assertEquals(HttpStatus.UNPROCESSABLE_ENTITY, result.getStatusCode());
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
		return restTemplate.getForEntity("http://localhost:" + port + url, String.class);
	}
}
