package org.example.univer.controllers.rest;

import org.example.univer.dto.LectureTimeDto;
import org.example.univer.repositories.LectureTimeRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LectureTimeRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private LectureTimeRepository lectureTimeRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        lectureTimeRepository.deleteAll();
    }
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void whenGetAllLectureTimes_thenReturnList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/lecturetimes",
                List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void whenGetExistingLectureTime_thenReturnLectureTime() {
        LectureTimeDto lectureTimeDto = new LectureTimeDto();
        lectureTimeDto.setStartLecture(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        lectureTimeDto.setEndLecture(LocalDateTime.parse("2025-07-24 12:00:00", formatter));

        ResponseEntity<LectureTimeDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/lecturetimes",
                lectureTimeDto,
                LectureTimeDto.class);

        Long createdId = createResponse.getBody().getId();

        ResponseEntity<LectureTimeDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/lecturetimes/" + createdId,
                LectureTimeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdId);
        assertThat(response.getBody().getStartLecture()).isEqualTo(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        assertThat(response.getBody().getEndLecture()).isEqualTo(LocalDateTime.parse("2025-07-24 12:00:00", formatter));

    }

    @Test
    void whenCreateNewLectureTime_thenReturnCreated() {
        LectureTimeDto lectureTimeDto = new LectureTimeDto();
        lectureTimeDto.setStartLecture(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        lectureTimeDto.setEndLecture(LocalDateTime.parse("2025-07-24 12:00:00", formatter));

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LectureTimeDto> request = new HttpEntity<>(lectureTimeDto, headers);

        ResponseEntity<LectureTimeDto> response = restTemplate.postForEntity("/api/lecturetimes", request, LectureTimeDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        LectureTimeDto createdDto = response.getBody();
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isNotNull();
    }

    @Test
    void whenUpdateExistingLectureTime_thenReturnOk() {
        LectureTimeDto lectureTimeDto = new LectureTimeDto();
        lectureTimeDto.setStartLecture(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        lectureTimeDto.setEndLecture(LocalDateTime.parse("2025-07-24 12:00:00", formatter));

        ResponseEntity<LectureTimeDto> createResponse = restTemplate.postForEntity("/api/lecturetimes", lectureTimeDto, LectureTimeDto.class);
        Long id = createResponse.getBody().getId();

        lectureTimeDto.setId(id);
        lectureTimeDto.setStartLecture(LocalDateTime.parse("2025-07-24 10:30:00", formatter));

        HttpEntity<LectureTimeDto> request = new HttpEntity<>(lectureTimeDto);
        ResponseEntity<LectureTimeDto> updateResponse = restTemplate.exchange(
                "/api/lecturetimes/" + id,
                HttpMethod.PUT,
                request,
                LectureTimeDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        lectureTimeDto.setStartLecture(LocalDateTime.parse("2025-07-24 10:30:00", formatter));
    }

    @Test
    void whenDeleteLectureTime_thenReturnNoContent() {
        LectureTimeDto lectureTimeDto = new LectureTimeDto();
        lectureTimeDto.setStartLecture(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        lectureTimeDto.setEndLecture(LocalDateTime.parse("2025-07-24 12:00:00", formatter));

        ResponseEntity<LectureTimeDto> createResponse = restTemplate.postForEntity("/api/lecturetimes", lectureTimeDto, LectureTimeDto.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/lecturetimes/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenGetNonExistingLectureTime_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/lecturetimes/999999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}
