package org.example.univer.controllers.rest_integration;

import org.example.univer.dto.CathedraDto;
import org.example.univer.dto.GroupDto;
import org.example.univer.dto.StudentDto;
import org.example.univer.models.Cathedra;
import org.example.univer.models.Gender;
import org.example.univer.models.Group;
import org.example.univer.repositories.CathedraRepository;
import org.example.univer.repositories.GroupRepository;
import org.example.univer.repositories.StudentRepository;
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
public class StudentRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private StudentRepository studentRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private CathedraRepository cathedraRepository;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        groupRepository.deleteAll();
        studentRepository.deleteAll();
    }

    @Test
    void whenGetAllStudents_thenReturnList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/students",
                List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(0);
    }


    @Test
    void whenGetExistingGroup_thenReturnGroup() {
        StudentDto studentDto = createStudent();

        ResponseEntity<StudentDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/students",
                studentDto,
                StudentDto.class);

        Long createdId = createResponse.getBody().getId();

        ResponseEntity<StudentDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/students/" + createdId,
                StudentDto.class);


        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        StudentDto result = response.getBody();
        assertThat(response.getBody().getId()).isEqualTo(createdId);
        assertThat(response.getBody().getFirstName()).isEqualTo("Igor");
        assertThat(result.getGroup().getName()).isEqualTo("Alpha");
    }

    @Test
    void whenCreateNewStudent_thenReturnCreated() {
        StudentDto studentDto = createStudent();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<StudentDto> request = new HttpEntity<>(studentDto, headers);

        ResponseEntity<StudentDto> response = restTemplate.postForEntity("/api/students", request, StudentDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        StudentDto createdDto = response.getBody();
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isNotNull();
    }

    @Test
    void whenUpdateExistingStudent_thenReturnOk() {
        StudentDto studentDto = createStudent();

        ResponseEntity<StudentDto> createResponse = restTemplate.postForEntity("/api/students", studentDto, StudentDto.class);
        Long id = createResponse.getBody().getId();

        studentDto.setId(id);
        studentDto.setFirstName("test 2");

        HttpEntity<StudentDto> request = new HttpEntity<>(studentDto);
        ResponseEntity<StudentDto> updateResponse = restTemplate.exchange(
                "/api/students/" + id,
                HttpMethod.PUT,
                request,
                StudentDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getFirstName()).isEqualTo("test 2");
    }

    @Test
    void whenDeleteGroup_thenReturnNoContent() {
        StudentDto studentDto = createStudent();

        ResponseEntity<StudentDto> createResponse = restTemplate.postForEntity("/api/students", studentDto, StudentDto.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/students/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenGetNonExistingGroup_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/students/999999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private StudentDto createStudent() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Math");
        cathedra = cathedraRepository.save(cathedra);

        Group group = new Group();
        group.setName("Alpha");
        group.setCathedra(cathedra);
        group = groupRepository.save(group);

        CathedraDto cDto = new CathedraDto();
        cDto.setId(cathedra.getId());
        cDto.setName(cathedra.getName());

        GroupDto gDto = new GroupDto();
        gDto.setId(group.getId());
        gDto.setName(group.getName());
        gDto.setCathedra(cDto);

        StudentDto studentDto = new StudentDto();
        studentDto.setGroup(gDto);
        studentDto.setGender(Gender.MALE);
        studentDto.setAddress("London");
        studentDto.setEmail("aaaa@mail.ru");
        studentDto.setPhone("+79001234567");
        studentDto.setBirthday(LocalDate.parse("1991-12-10"));
        studentDto.setFirstName("Igor");
        studentDto.setLastName("Paragvaev");

        return studentDto;
    }
}
