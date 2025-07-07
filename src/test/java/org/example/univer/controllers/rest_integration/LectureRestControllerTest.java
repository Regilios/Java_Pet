package org.example.univer.controllers.rest_integration;

import org.example.univer.dto.*;
import org.example.univer.models.*;
import org.example.univer.repositories.*;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("test")
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
public class LectureRestControllerTest {
    @Autowired
    private TestRestTemplate restTemplate;
    @Autowired
    private LectureRepository lectureRepository;
    @Autowired
    private GroupRepository groupRepository;
    @Autowired
    private TeacherRepository teacherRepository;
    @Autowired
    private CathedraRepository cathedraRepository;
    @Autowired
    private SubjectRepository subjectRepository;
    @Autowired
    private LectureTimeRepository lectureTimeRepository;
    @Autowired
    private AudienceRepository audienceRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @LocalServerPort
    private int port;

    @BeforeEach
    void setUp() {
        lectureRepository.deleteAll();
        groupRepository.deleteAll();
        teacherRepository.deleteAll();
        subjectRepository.deleteAll();
        lectureTimeRepository.deleteAll();
        audienceRepository.deleteAll();
        cathedraRepository.deleteAll();
    }

    @Test
    void whenGetAllLectures_thenReturnList() {
        ResponseEntity<List> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/lectures",
                List.class);
        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(response.getBody()).isNotNull();
        assertThat(response.getBody().size()).isGreaterThanOrEqualTo(0);
    }

    @Test
    void whenGetExistingLecture_thenReturnLecture() {
        LectureDto lectureDto = buildLectureDto();

        ResponseEntity<LectureDto> createResponse = restTemplate.postForEntity(
                "http://localhost:" + port + "/api/lectures",
                lectureDto,
                LectureDto.class);

        Long createdId = createResponse.getBody().getId();

        ResponseEntity<LectureDto> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/lectures/" + createdId,
                LectureDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.OK);
        LectureDto result = response.getBody();
        assertThat(result).isNotNull();
        assertThat(result.getId()).isEqualTo(createdId);
        assertThat(result.getCathedra()).isNotNull().satisfies(c -> assertThat(c.getId()).isNotNull());
        assertThat(result.getTeacher()).isNotNull().satisfies(t -> assertThat(t.getId()).isNotNull());
        assertThat(result.getSubject()).isNotNull().satisfies(s -> assertThat(s.getId()).isNotNull());
        assertThat(result.getAudience()).isNotNull().satisfies(a -> assertThat(a.getId()).isNotNull());
        assertThat(result.getTime()).isNotNull().satisfies(t -> assertThat(t.getId()).isNotNull());
        assertThat(result.getGroups()).isNotEmpty();
        assertThat(result.getCathedra().getName()).isEqualTo("Math");
        assertThat(result.getTeacher().getFirstName()).isEqualTo("Alice");
        assertThat(result.getAudience().getRoomNumber()).isEqualTo(304);
        assertThat(result.getSubject().getName()).isEqualTo("Algebra");
        assertThat(result.getTime().getStartLecture()).isEqualTo(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        assertThat(result.getGroups().get(0).getName()).isEqualTo("Alpha");
    }


    @Test
    void whenCreateNewLecture_thenReturnCreated() {
        LectureDto lectureDto = buildLectureDto();

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<LectureDto> request = new HttpEntity<>(lectureDto, headers);

        ResponseEntity<LectureDto> response = restTemplate.postForEntity("/api/lectures", request, LectureDto.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.CREATED);
        assertThat(response.getHeaders().getLocation()).isNotNull();
        LectureDto createdDto = response.getBody();
        assertThat(createdDto).isNotNull();
        assertThat(createdDto.getId()).isNotNull();
    }

    @Test
    void whenUpdateExistingLecture_thenReturnOk() {
        LectureDto lectureDto = buildLectureDto();

        ResponseEntity<LectureDto> createResponse = restTemplate.postForEntity("/api/lectures", lectureDto, LectureDto.class);
        Long id = createResponse.getBody().getId();

        Cathedra cathedra = new Cathedra();
        cathedra.setName("World");
        cathedra = cathedraRepository.save(cathedra);

        CathedraDto cDto = new CathedraDto();
        cDto.setId(cathedra.getId());
        cDto.setName(cathedra.getName());

        lectureDto.setId(id);
        lectureDto.setCathedra(cDto);

        HttpEntity<LectureDto> request = new HttpEntity<>(lectureDto);
        ResponseEntity<LectureDto> updateResponse = restTemplate.exchange(
                "/api/lectures/" + id,
                HttpMethod.PUT,
                request,
                LectureDto.class
        );

        assertThat(updateResponse.getStatusCode()).isEqualTo(HttpStatus.OK);
        assertThat(updateResponse.getBody().getCathedra().getName()).isEqualTo("World");
    }


    @Test
    void whenDeleteLecture_thenReturnNoContent() {
        LectureDto lectureDto = buildLectureDto();

        ResponseEntity<LectureDto> createResponse = restTemplate.postForEntity("/api/lectures", lectureDto, LectureDto.class);
        Long id = createResponse.getBody().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
                "/api/lectures/" + id,
                HttpMethod.DELETE,
                null,
                Void.class
        );

        assertThat(deleteResponse.getStatusCode()).isEqualTo(HttpStatus.NO_CONTENT);
    }

    @Test
    void whenGetNonExistingLecture_thenReturnNotFound() {
        ResponseEntity<String> response = restTemplate.getForEntity(
                "http://localhost:" + port + "/api/lectures/999999",
                String.class);

        assertThat(response.getStatusCode()).isEqualTo(HttpStatus.NOT_FOUND);
    }

    private LectureDto buildLectureDto() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Math");
        cathedra = cathedraRepository.save(cathedra);

        Group group = new Group();
        group.setName("Alpha");
        group.setCathedra(cathedra);
        group = groupRepository.save(group);

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

        Audience audience = new Audience();
        audience.setRoomNumber(304);
        audience.setCapacity(45);
        audience = audienceRepository.save(audience);

        LectureTime time = new LectureTime();
        time.setStartLecture(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        time.setEndLecture(LocalDateTime.parse("2025-07-24 11:00:00", formatter));
        time = lectureTimeRepository.save(time);

        CathedraDto cDto = new CathedraDto();
        cDto.setId(cathedra.getId());
        cDto.setName(cathedra.getName());

        GroupDto gDto = new GroupDto();
        gDto.setId(group.getId());
        gDto.setName(group.getName());
        gDto.setCathedra(cDto);

        SubjectDto sDto = new SubjectDto();
        sDto.setId(subject.getId());
        sDto.setName(subject.getName());
        sDto.setDescription(subject.getDescription());

        AudienceDto aDto = new AudienceDto();
        aDto.setId(audience.getId());
        aDto.setRoomNumber(audience.getRoomNumber());
        aDto.setCapacity(audience.getCapacity());

        LectureTimeDto tDto = new LectureTimeDto();
        tDto.setId(time.getId());
        tDto.setStartLecture(time.getStartLecture());
        tDto.setEndLecture(time.getEndLecture());

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

        LectureDto dto = new LectureDto();
        dto.setCathedra(cDto);
        dto.setGroups(Collections.singletonList(gDto));
        dto.setSubject(sDto);
        dto.setTime(tDto);
        dto.setAudience(aDto);
        dto.setTeacher(teacherDto);
        dto.setGroupIds(Collections.singletonList(group.getId()));

        return dto;
    }
}
