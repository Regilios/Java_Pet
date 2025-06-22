package org.example.univer.controllers.rest;

import org.example.univer.dto.SubjectDto;
import org.example.univer.repositories.SubjectRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.http.*;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class SubjectRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SubjectRepository subjectRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        subjectRepository.deleteAll();
    }

    @Test
    void whenGetAllSubjects_thenReturnList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/subjects",
                List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void whenGetExistingSubject_thenReturnAudience() {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("test");
        subjectDto.setDescription("test test test test test test");

        ResponseEntity<SubjectDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/subjects",
                subjectDto,
                SubjectDto.class);

        Long createdId = createResponse.getBody().getId();

        ResponseEntity<SubjectDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/subjects/" + createdId,
                SubjectDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdId);
        assertThat(response.getBody().getName()).isEqualTo("test");
        assertThat(response.getBody().getDescription()).isEqualTo("test test test test test test");
    }

    @Test
    void whenCreateNewSubject_thenReturnCreated() {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("test");
        subjectDto.setDescription("test test test test test test");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<SubjectDto> request = new HttpEntity<>(subjectDto, headers);

        ResponseEntity<SubjectDto> response = restTemplate.postForEntity("/api/subjects", request, SubjectDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        SubjectDto createdDto = response.getBody();
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isNotNull();
    }

    @Test
    void whenUpdateExistingSubject_thenReturnOk() {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("test");
        subjectDto.setDescription("test test test test test test");

        ResponseEntity<SubjectDto> createResponse = restTemplate.postForEntity("/api/subjects", subjectDto, SubjectDto.class);
        Long id = createResponse.getBody().getId();

        subjectDto.setId(id);
        subjectDto.setName("test 2");

        HttpEntity<SubjectDto> request = new HttpEntity<>(subjectDto);
        ResponseEntity<SubjectDto> updateResponse = restTemplate.exchange(
                "/api/subjects/" + id,
                HttpMethod.PUT,
                request,
                SubjectDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getName()).isEqualTo("test 2");
    }

    @Test
    void whenDeleteSubject_thenReturnNoContent() {
        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setName("test");
        subjectDto.setDescription("test test test test test test");

        ResponseEntity<SubjectDto> createResponse = restTemplate.postForEntity("/api/subjects", subjectDto, SubjectDto.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/subjects/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenGetNonExistingSubject_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/subjects/999999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

}
