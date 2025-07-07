package org.example.univer.controllers.rest_integration;

import org.example.univer.dto.CathedraDto;
import org.example.univer.dto.SubjectDto;
import org.example.univer.dto.TeacherDto;
import org.example.univer.dto.VacationDto;
import org.example.univer.models.Cathedra;
import org.example.univer.models.Gender;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.example.univer.repositories.CathedraRepository;
import org.example.univer.repositories.SubjectRepository;
import org.example.univer.repositories.TeacherRepository;
import org.example.univer.repositories.VacationRepository;
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
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class VacationRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private VacationRepository vacationRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CathedraRepository cathedraRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        vacationRepository.deleteAll();
        teacherRepository.deleteAll();
        cathedraRepository.deleteAll();
        subjectRepository.deleteAll();
    }

    @Test
    void whenGetAllVacations_thenReturnList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/vacations",
                List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void whenGetExistingVacation_thenReturnVacation() {
        VacationDto vacationDto = createVacation();

        ResponseEntity<VacationDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/vacations",
                vacationDto,
                VacationDto.class);

        Long createdId = createResponse.getBody().getId();

        ResponseEntity<VacationDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/vacations/" + createdId,
                VacationDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().getId()).isEqualTo(createdId);
        assertThat(response.getBody().getStartJob()).isEqualTo(LocalDate.parse("2025-10-01"));
        assertThat(response.getBody().getEndJob()).isEqualTo(LocalDate.parse("2025-10-11"));
    }

    @Test
    void whenCreateNewVacation_thenReturnCreated() {
        VacationDto vacationDto = createVacation();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<VacationDto> request = new HttpEntity<>(vacationDto, headers);

        ResponseEntity<VacationDto> response = restTemplate.postForEntity("/api/vacations", request, VacationDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        VacationDto createdDto = response.getBody();
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isNotNull();
    }

    @Test
    void whenUpdateExistingVacation_thenReturnOk() {
        VacationDto vacationDto = createVacation();

        ResponseEntity<VacationDto> createResponse = restTemplate.postForEntity("/api/vacations", vacationDto, VacationDto.class);
        Long id = createResponse.getBody().getId();

        vacationDto.setId(id);
        vacationDto.setStartJob(LocalDate.parse("2025-10-02"));

        HttpEntity<VacationDto> request = new HttpEntity<>(vacationDto);
        ResponseEntity<VacationDto> updateResponse = restTemplate.exchange(
                "/api/vacations/" + id,
                HttpMethod.PUT,
                request,
                VacationDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getStartJob()).isEqualTo(LocalDate.parse("2025-10-02"));
    }


    @Test
    void whenDeleteVacation_thenReturnNoContent() {
        VacationDto vacationDto = createVacation();

        ResponseEntity<VacationDto> createResponse = restTemplate.postForEntity("/api/vacations", vacationDto, VacationDto.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/vacations/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenGetNonExistingVacation_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/vacations/999999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }
    private VacationDto createVacation() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Math");
        cathedra = cathedraRepository.save(cathedra);

        Subject subject = new Subject();
        subject.setName("Algebra");
        subject.setDescription("Advanced algebra course");
        subject = subjectRepository.save(subject);

        Teacher teacher = new Teacher();
        teacher.setFirstName("Alice");
        teacher.setLastName("Smith");
        teacher.setEmail("alice@example.com");
        teacher.setPhone("+79001234567");
        teacher.setAddress("London");
        teacher.setGender(Gender.FEMALE);
        teacher.setBirthday(LocalDate.now().minusYears(30));
        teacher.setCathedra(cathedra);
        teacher.setSubjects(Collections.singletonList(subject));
        teacher = teacherRepository.save(teacher);

        CathedraDto cDto = new CathedraDto();
        cDto.setId(cathedra.getId());
        cDto.setName(cathedra.getName());

        SubjectDto sDto = new SubjectDto();
        sDto.setId(subject.getId());
        sDto.setName(subject.getName());
        sDto.setDescription(subject.getDescription());

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(teacher.getId());
        teacherDto.setFirstName(teacher.getFirstName());
        teacherDto.setLastName(teacher.getLastName());
        teacherDto.setEmail(teacher.getEmail());
        teacherDto.setPhone(teacher.getPhone());
        teacherDto.setAddress(teacher.getAddress());
        teacherDto.setGender(teacher.getGender());
        teacherDto.setBirthday(teacher.getBirthday());
        teacherDto.setCathedra(cDto);
        teacherDto.setSubjectIds(teacher.getSubjects().stream().map(Subject::getId).toList());

        VacationDto vacationDto = new VacationDto();
        vacationDto.setStartJob(LocalDate.parse("2025-10-01"));
        vacationDto.setEndJob(LocalDate.parse("2025-10-11"));
        vacationDto.setTeacherId(teacherDto.getId());

        return vacationDto;
    }
}
