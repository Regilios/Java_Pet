package org.example.univer.controllers.rest_integration;

import org.example.univer.dto.HolidayDto;
import org.example.univer.repositories.HolidayRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class HolidayRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private HolidayRepository holidayRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        holidayRepository.deleteAll();
    }

    @Test
    void whenGetAllHolidays_thenReturnList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/holidays",
                List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void whenGetExistingHoliday_thenReturnHoliday() {
        HolidayDto holidayDto = new HolidayDto();
        holidayDto.setStartHoliday(LocalDate.parse("2025-10-13"));
        holidayDto.setEndHoliday(LocalDate.parse("2025-10-20"));
        holidayDto.setDescription("test");

        ResponseEntity<HolidayDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/holidays",
                holidayDto,
                HolidayDto.class);

        Long createdId = createResponse.getBody().getId();

        ResponseEntity<HolidayDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/holidays/" + createdId,
                HolidayDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdId);
        assertThat(response.getBody().getStartHoliday()).isEqualTo(LocalDate.parse("2025-10-13"));
        assertThat(response.getBody().getDescription()).isEqualTo("test");
    }

    @Test
    void whenCreateNewHoliday_thenReturnCreated() {
        HolidayDto holidayDto = new HolidayDto();
        holidayDto.setStartHoliday(LocalDate.parse("2025-10-13"));
        holidayDto.setEndHoliday(LocalDate.parse("2025-10-20"));
        holidayDto.setDescription("test");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<HolidayDto> request = new HttpEntity<>(holidayDto, headers);

        ResponseEntity<HolidayDto> response = restTemplate.postForEntity("/api/holidays", request, HolidayDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        HolidayDto createdDto = response.getBody();
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isNotNull();
    }

    @Test
    void whenUpdateExistingHoliday_thenReturnOk() {
        HolidayDto holidayDto = new HolidayDto();
        holidayDto.setStartHoliday(LocalDate.parse("2025-10-13"));
        holidayDto.setEndHoliday(LocalDate.parse("2025-10-20"));
        holidayDto.setDescription("test");

        ResponseEntity<HolidayDto> createResponse = restTemplate.postForEntity("/api/holidays", holidayDto, HolidayDto.class);
        Long id = createResponse.getBody().getId();

        holidayDto.setId(id);
        holidayDto.setDescription("test 2");

        HttpEntity<HolidayDto> request = new HttpEntity<>(holidayDto);
        ResponseEntity<HolidayDto> updateResponse = restTemplate.exchange(
                "/api/holidays/" + id,
                HttpMethod.PUT,
                request,
                HolidayDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getDescription()).isEqualTo("test 2");
    }

    @Test
    void whenDeleteHoliday_thenReturnNoContent() {
        HolidayDto holidayDto = new HolidayDto();
        holidayDto.setStartHoliday(LocalDate.parse("2025-10-13"));
        holidayDto.setEndHoliday(LocalDate.parse("2025-10-20"));
        holidayDto.setDescription("test");

        ResponseEntity<HolidayDto> createResponse = restTemplate.postForEntity("/api/holidays", holidayDto, HolidayDto.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/holidays/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenGetNonExistingHoliday_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/holidays/999999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
