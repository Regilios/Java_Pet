package org.example.univer.controllers.rest;

import org.example.univer.dto.CathedraDto;
import org.example.univer.dto.GroupDto;
import org.example.univer.models.Cathedra;
import org.example.univer.repositories.CathedraRepository;
import org.example.univer.repositories.GroupRepository;
import org.example.univer.repositories.TeacherRepository;
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
public class GroupRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private CathedraRepository cathedraRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        groupRepository.deleteAll();
        cathedraRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @Test
    void whenGetAllGroups_thenReturnList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/groups",
                List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void whenGetExistingGroup_thenReturnGroup() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");
        cathedra = cathedraRepository.save(cathedra);

        GroupDto groupDto = new GroupDto();
        groupDto.setName("test-group");
        CathedraDto cathedraDto = new CathedraDto();
        cathedraDto.setId(cathedra.getId());
        cathedraDto.setName(cathedra.getName());
        groupDto.setCathedra(cathedraDto);

        ResponseEntity<GroupDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/groups",
                groupDto,
                GroupDto.class);

        Long createdId = createResponse.getBody().getId();

        ResponseEntity<GroupDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/groups/" + createdId,
                GroupDto.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdId);
        assertThat(response.getBody().getName()).isEqualTo("test-group");
        assertThat(response.getBody().getCathedra().getId()).isEqualTo(cathedraDto.getId());
        assertThat(response.getBody().getCathedra().getName()).isEqualTo(cathedraDto.getName());
    }

    @Test
    void whenCreateNewGroup_thenReturnCreated() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");
        cathedra = cathedraRepository.save(cathedra);

        GroupDto groupDto = new GroupDto();
        groupDto.setName("test-group");
        CathedraDto cathedraDto = new CathedraDto();
        cathedraDto.setId(cathedra.getId());
        cathedraDto.setName(cathedra.getName());
        groupDto.setCathedra(cathedraDto);

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<GroupDto> request = new HttpEntity<>(groupDto, headers);

        ResponseEntity<GroupDto> response = restTemplate.postForEntity("/api/groups", request, GroupDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        GroupDto createdDto = response.getBody();
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isNotNull();
    }

    @Test
    void whenUpdateExistingGroup_thenReturnOk() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");
        cathedra = cathedraRepository.save(cathedra);

        GroupDto groupDto = new GroupDto();
        groupDto.setName("test-group");
        CathedraDto cathedraDto = new CathedraDto();
        cathedraDto.setId(cathedra.getId());
        cathedraDto.setName(cathedra.getName());
        groupDto.setCathedra(cathedraDto);

        ResponseEntity<GroupDto> createResponse = restTemplate.postForEntity("/api/groups", groupDto, GroupDto.class);
        Long id = createResponse.getBody().getId();

        groupDto.setId(id);
        groupDto.setName("test 2");

        HttpEntity<GroupDto> request = new HttpEntity<>(groupDto);
        ResponseEntity<GroupDto> updateResponse = restTemplate.exchange(
                "/api/groups/" + id,
                HttpMethod.PUT,
                request,
                GroupDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getName()).isEqualTo("test 2");
    }

    @Test
    void whenDeleteGroup_thenReturnNoContent() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");
        cathedra = cathedraRepository.save(cathedra);

        GroupDto groupDto = new GroupDto();
        groupDto.setName("test-group");
        CathedraDto cathedraDto = new CathedraDto();
        cathedraDto.setId(cathedra.getId());
        cathedraDto.setName(cathedra.getName());
        groupDto.setCathedra(cathedraDto);

        ResponseEntity<GroupDto> createResponse = restTemplate.postForEntity("/api/groups", groupDto, GroupDto.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/groups/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenGetNonExistingGroup_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/groups/999999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
}


