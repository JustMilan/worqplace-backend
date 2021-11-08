package com.quintor.worqplace.presentation;

import com.quintor.worqplace.CiTestConfiguration;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import static org.junit.jupiter.api.Assertions.*;

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

		assertEquals(200, result.getStatusCode().value());
		assertNotNull(result.getBody());
		assertNotEquals("[]", result.getBody());
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

		assertEquals(422, result.getStatusCode().value());
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

		assertEquals(200, result.getStatusCode().value());
		assertNotNull(result.getBody());
		assertNotEquals("[]", result.getBody());
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

		assertEquals(422, result.getStatusCode().value());
	}

	/**
	 * Function that uses the {@link TestRestTemplate} to send a GET request
	 * to the Back-End for testing during Continuous Integration.
	 *
	 * @param url	the URL Path after "https://localhost:8080".
	 *              as a {@link String}
	 * @return 		a {@link ResponseEntity} for the request.
	 */
	ResponseEntity<String> getRequest(String url) {
		return restTemplate.getForEntity("http://localhost:" + port + url, String.class);
	}


}
