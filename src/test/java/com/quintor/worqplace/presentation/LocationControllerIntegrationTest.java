package com.quintor.worqplace.presentation;

import com.quintor.worqplace.CiTestConfiguration;
import com.quintor.worqplace.WorqplaceApplication;
import com.quintor.worqplace.domain.Address;
import com.quintor.worqplace.domain.Location;
import com.quintor.worqplace.presentation.dto.location.LocationDTO;
import com.quintor.worqplace.presentation.dto.location.LocationMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.web.server.LocalServerPort;
import org.springframework.context.annotation.Import;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("ci")
@Import(CiTestConfiguration.class)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class LocationControllerIntegrationTest {

	@LocalServerPort
	private int port;

	@Autowired
	private TestRestTemplate restTemplate;

	private Location amersfoort;
	private Location denBosch;
	private Location deventer;
	private Location denHaag;
	private Location groningen;

	@BeforeEach
	void initialize() {
		this.amersfoort = new Location(1L, "Quintor Amersfoort", new Address(1L, 14, "m", "Maanlander", "3824 MP", "Amersfoort"), List.of());
		this.denBosch = new Location(2L, "Quintor Den Bosch", new Address(2L, 1, "", "Havensingel", "5211 TX", "Den Bosch"), List.of());
		this.deventer = new Location(3L, "Quintor Deventer", new Address(3L, 6, "", "Zutphenseweg", "7418 AJ", "Deventer"), List.of());
		this.denHaag = new Location(4L, "Quintor Den Haag", new Address(4L, 4, "-5", "Lange Vijverberg", "2513 AC", "Den Haag"), List.of());
		this.groningen = new Location(5L, "Quintor Groningen", new Address(5L, 112, "", "Ubbo Emmiussingel", "9711 BK", "Groningen"), List.of());
	}

	@Test
	void contextLoads() {
		ResponseEntity<List<LocationDTO>> responseEntity =
				new ResponseEntity<>(Stream.of(amersfoort, denBosch, deventer, denHaag, groningen)
						.map(LocationMapper.INSTANCE::toLocationDTO)
						.collect(Collectors.toList()), HttpStatus.OK);

		String formattedResponse = Objects.requireNonNull(responseEntity.getBody())
				.toString()
				.replaceAll(",\\s", ",");

		assertEquals(formattedResponse,
				this.restTemplate.getForObject("http://localhost:" + port + "/locations", String.class));
	}
}
