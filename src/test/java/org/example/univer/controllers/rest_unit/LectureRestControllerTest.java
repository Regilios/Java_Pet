package org.example.univer.controllers.rest_unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.univer.controllers.rest.LectureRestController;
import org.example.univer.dto.*;
import org.example.univer.mappers.LectureMapper;
import org.example.univer.models.*;
import org.example.univer.services.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class LectureRestControllerTest {
    @InjectMocks
    private LectureRestController controller;
    @Mock
    private LectureService lectureService;
    @Mock
    private LectureMapper lectureMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private LectureDto lectureDto;
    private Lecture lecture;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());
        mapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        // Добавляем поддержку Pageable
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setCustomArgumentResolvers(pageableResolver)
                .setMessageConverters(new MappingJackson2HttpMessageConverter(mapper))
                .build();

        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        cathedra.setName("Math");

        Group group = new Group();
        group.setId(1L);
        group.setName("Alpha");
        group.setCathedra(cathedra);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Algebra");
        subject.setDescription("Advanced algebra course");

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Alice");
        teacher.setLastName("Smith");
        teacher.setEmail("alice@example.com");
        teacher.setPhone("+79001234567");
        teacher.setAddress("London");
        teacher.setGender(Gender.FEMALE);
        teacher.setBirthday(LocalDate.now().minusYears(30));
        teacher.setCathedra(cathedra);
        teacher.setSubjects(Collections.singletonList(subject));

        Audience audience = new Audience();
        audience.setId(1L);
        audience.setRoomNumber(304);
        audience.setCapacity(45);

        LectureTime time = new LectureTime();
        time.setId(1L);
        time.setStartLecture(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        time.setEndLecture(LocalDateTime.parse("2025-07-24 11:00:00", formatter));

        lecture = new Lecture();
        lecture.setId(1L);
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(time);
        lecture.setAudience(audience);
        lecture.setGroups(List.of(group));
        lecture.setCathedra(cathedra);

        CathedraDto cathedraDto = new CathedraDto();
        cathedraDto.setId(1L);
        cathedraDto.setName("Math");

        GroupDto groupDto = new GroupDto();
        groupDto.setId(1L);
        groupDto.setName("Alpha");
        groupDto.setCathedra(cathedraDto);

        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setName("Algebra");
        subjectDto.setDescription("Advanced algebra course");

        TeacherDto teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("Alice");
        teacherDto.setLastName("Smith");
        teacherDto.setEmail("alice@example.com");
        teacherDto.setPhone("+79001234567");
        teacherDto.setAddress("London");
        teacherDto.setGender(Gender.FEMALE);
        teacherDto.setBirthday(LocalDate.now().minusYears(30));
        teacherDto.setCathedra(cathedraDto);
        teacherDto.setSubjects(Collections.singletonList(subjectDto));

        AudienceDto audienceDto = new AudienceDto();
        audienceDto.setId(1L);
        audienceDto.setRoomNumber(304);
        audienceDto.setCapacity(45);

        LectureTimeDto timeDto = new LectureTimeDto();
        timeDto.setId(1L);
        timeDto.setStartLecture(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        timeDto.setEndLecture(LocalDateTime.parse("2025-07-24 11:00:00", formatter));

        lectureDto = new LectureDto();
        lectureDto.setId(1L);
        lectureDto.setTeacher(teacherDto);
        lectureDto.setSubject(subjectDto);
        lectureDto.setTime(timeDto);
        lectureDto.setAudience(audienceDto);
        lectureDto.setGroups(List.of(groupDto));
        lectureDto.setCathedra(cathedraDto);
        lectureDto.setGroupIds(List.of(1L));
    }

    @Test
    void whenGetAll_thenReturnList() throws Exception {
        List<LectureDto> lectureDtos = List.of(lectureDto);

        when(lectureService.findAllWithGroups(any(Pageable.class)))
                .thenReturn(new PageImpl<>(lectureDtos));

        mockMvc.perform(get("/api/lectures"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].teacher.firstName").value("Alice"))
                .andExpect(jsonPath("$[0].groups[0].name").value("Alpha"))
                .andExpect(jsonPath("$[0].time.startLecture").exists());
    }

    @Test
    void whenCreate_thenReturnCreated() throws Exception {
        ObjectMapper configuredMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(lectureMapper.toEntity(any())).thenReturn(lecture);
        when(lectureService.create(any())).thenReturn(lecture);
        when(lectureMapper.toDto(any())).thenReturn(lectureDto);

        mockMvc.perform(post("/api/lectures")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(configuredMapper.writeValueAsString(lectureDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.teacher.firstName").value("Alice"))
                .andExpect(jsonPath("$.groups[0].name").value("Alpha"))
                .andExpect(jsonPath("$.time.startLecture").exists());

        verify(lectureService).create(lecture);
    }

    @Test
    void whenGetById_thenReturnLecture() throws Exception {
        when(lectureService.findEntityById(1L)).thenReturn(Optional.of(lectureDto));

        mockMvc.perform(get("/api/lectures/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.teacher.firstName").value("Alice"))
                .andExpect(jsonPath("$.groups[0].name").value("Alpha"))
                .andExpect(jsonPath("$.time.startLecture").exists());
    }

    @Test
    void whenUpdate_thenReturnUpdated() throws Exception {
        ObjectMapper configuredMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(lectureMapper.toEntity(any())).thenReturn(lecture);
        when(lectureService.update(any())).thenReturn(lecture);

        TeacherDto teacherDto = lectureDto.getTeacher();
        teacherDto.setFirstName("Olga");
        lectureDto.setTeacher(teacherDto);

        mockMvc.perform(put("/api/lectures/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(configuredMapper.writeValueAsString(lectureDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.teacher.firstName").value("Olga"));

    }

    @Test
    void whenDelete_thenReturnNoContent() throws Exception {
            doNothing().when(lectureService).deleteById(1L);

        mockMvc.perform(delete("/api/lectures/1"))
                .andExpect(status().isNoContent());
    }
}
