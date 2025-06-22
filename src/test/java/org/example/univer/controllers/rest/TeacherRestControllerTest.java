package org.example.univer.controllers.rest;

import org.example.univer.dto.CathedraDto;
import org.example.univer.dto.LectureDto;
import org.example.univer.dto.SubjectDto;
import org.example.univer.dto.TeacherDto;
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
public class TeacherRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private VacationRepository vacationRepository;
    @Autowired
    private CathedraRepository cathedraRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @BeforeEach
    void setUp() {
        teacherRepository.deleteAll();
        cathedraRepository.deleteAll();
        vacationRepository.deleteAll();
        subjectRepository.deleteAll();
        teacherRepository.deleteAll();
    }

    @LocalServerPort
    private int port;

    @Test
    void whenGetAllTeachers_thenReturnList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/teachers",
                List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void whenGetExistingTeacher_thenReturnLecture() {
        TeacherDto teacherDto = createTeacher();

        ResponseEntity<TeacherDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/teachers",
                teacherDto,
                TeacherDto.class);

        Long createdId = createResponse.getBody().getId();

        ResponseEntity<TeacherDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/teachers/" + createdId,
                TeacherDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        TeacherDto result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(createdId);
        assertThat(result.getCathedra().getName()).isEqualTo("Math");
        assertThat(result.getEmail()).isEqualTo("alice@example.com");
        assertThat(result.getSubjects().get(0).getName()).isEqualTo("Algebra");
    }

    @Test
    void whenCreateNewTeacher_thenReturnCreated() {
        TeacherDto teacherDto = createTeacher();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<TeacherDto> request = new HttpEntity<>(teacherDto, headers);

        ResponseEntity<TeacherDto> response = restTemplate.postForEntity("/api/teachers", request, TeacherDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        TeacherDto createdDto = response.getBody();
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isNotNull();
    }

    @Test
    void whenUpdateExistingTeacher_thenReturnOk() {
        TeacherDto teacherDto = createTeacher();

        ResponseEntity<TeacherDto> createResponse = restTemplate.postForEntity("/api/teachers", teacherDto, TeacherDto.class);
        Long id = createResponse.getBody().getId();

        Cathedra cathedra = new Cathedra();
        cathedra.setName("World");
        cathedra = cathedraRepository.save(cathedra);

        CathedraDto cDto = new CathedraDto();
        cDto.setId(cathedra.getId());
        cDto.setName(cathedra.getName());

        teacherDto.setId(id);
        teacherDto.setCathedra(cDto);

        HttpEntity<TeacherDto> request = new HttpEntity<>(teacherDto);
        ResponseEntity<TeacherDto> updateResponse = restTemplate.exchange(
                "/api/teachers/" + id,
                HttpMethod.PUT,
                request,
                TeacherDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getCathedra().getName()).isEqualTo("World");
    }


    @Test
    void whenDeleteTeacher_thenReturnNoContent() {
        TeacherDto teacherDto = createTeacher();

        ResponseEntity<LectureDto> createResponse = restTemplate.postForEntity("/api/teachers", teacherDto, LectureDto.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/teachers/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenGetNonExistingTeacher_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/teachers/999999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private TeacherDto createTeacher() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Math");
        cathedra = cathedraRepository.save(cathedra);

        Subject subject = new Subject();
        subject.setName("Algebra");
        subject.setDescription("Advanced algebra course");
        subject = subjectRepository.save(subject);

        Teacher teacher = new Teacher();
        teacher.setFirstName("Alexa");
        teacher.setLastName("Smith");
        teacher.setEmail("alice@example.com");
        teacher.setPhone("+79001234567");
        teacher.setAddress("London");
        teacher.setGender(Gender.FEMALE);
        teacher.setBirthday(LocalDate.now().minusYears(30));
        teacher.setCathedra(cathedra);
        teacher.setSubjects(Collections.singletonList(subject));


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

        return teacherDto;
    }
}