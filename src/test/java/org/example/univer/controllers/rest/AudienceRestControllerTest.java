package org.example.univer.controllers.rest;

import org.example.univer.dto.AudienceDto;
import org.example.univer.repositories.AudienceRepository;
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
class AudienceRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private AudienceRepository audienceRepository;

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        audienceRepository.deleteAll();
    }

    @Test
    void whenGetAllAudiences_thenReturnList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/audiences",
                List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void whenGetExistingAudience_thenReturnAudience() {
        AudienceDto newAudience = new AudienceDto();
        newAudience.setRoomNumber(525);
        newAudience.setCapacity(30);

        ResponseEntity<AudienceDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/audiences",
                newAudience,
                AudienceDto.class);

        Long createdId = createResponse.getBody().getId();

        ResponseEntity<AudienceDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/audiences/" + createdId,
                AudienceDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdId);
        assertThat(response.getBody().getRoomNumber()).isEqualTo(525);
        assertThat(response.getBody().getCapacity()).isEqualTo(30);
    }

    @Test
    void whenCreateNewAudience_thenReturnCreated() {
        AudienceDto dto = new AudienceDto();
        dto.setRoomNumber(505);
        dto.setCapacity(30);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<AudienceDto> request = new HttpEntity<>(dto, headers);

        ResponseEntity<AudienceDto> response = restTemplate.postForEntity("/api/audiences", request, AudienceDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        AudienceDto createdDto = response.getBody();
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isNotNull();
    }

    @Test
    void whenUpdateExistingAudience_thenReturnOk() {
        AudienceDto dto = new AudienceDto();
        dto.setRoomNumber(506);
        dto.setCapacity(30);

        ResponseEntity<AudienceDto> createResponse = restTemplate.postForEntity("/api/audiences", dto, AudienceDto.class);
        Long id = createResponse.getBody().getId();

        dto.setId(id);
        dto.setCapacity(35);

        HttpEntity<AudienceDto> request = new HttpEntity<>(dto);
        ResponseEntity<AudienceDto> updateResponse = restTemplate.exchange(
                "/api/audiences/" + id,
                HttpMethod.PUT,
                request,
                AudienceDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getCapacity()).isEqualTo(35);
    }

    @Test
    void whenDeleteAudience_thenReturnNoContent() {
        AudienceDto dto = new AudienceDto();
        dto.setRoomNumber(507);
        dto.setCapacity(30);

        ResponseEntity<AudienceDto> createResponse = restTemplate.postForEntity("/api/audiences", dto, AudienceDto.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/audiences/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenGetNonExistingAudience_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/audiences/999999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
        assertThat(response.getBody()).contains("Аудиенция не найдена");
    }
}