package org.example.univer.controllers.rest_unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.univer.controllers.rest.TeacherRestController;
import org.example.univer.dto.CathedraDto;
import org.example.univer.dto.SubjectDto;
import org.example.univer.dto.TeacherDto;
import org.example.univer.mappers.TeacherMapper;
import org.example.univer.models.Cathedra;
import org.example.univer.models.Gender;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.example.univer.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class TeacherRestControllerTest {
    @InjectMocks
    private TeacherRestController controller;
    @Mock
    private TeacherService teacherService;
    @Mock
    private TeacherMapper teacherMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private TeacherDto teacherDto;
    private Teacher teacher;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        cathedra.setName("Math");

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Test");
        subject.setDescription("test test test test test test");

        teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("Alexa");
        teacher.setLastName("Smith");
        teacher.setEmail("alice@example.com");
        teacher.setPhone("+79001234567");
        teacher.setAddress("London");
        teacher.setGender(Gender.FEMALE);
        teacher.setBirthday(LocalDate.now().minusYears(30));
        teacher.setCathedra(cathedra);
        teacher.setSubjects(Collections.singletonList(subject));

        SubjectDto subjectDto = new SubjectDto();
        subjectDto.setId(1L);
        subjectDto.setName("Test");
        subjectDto.setDescription("test test test test test test");

        CathedraDto cathedraDto = new CathedraDto();
        cathedraDto.setId(1L);
        cathedraDto.setName("Math");

        teacherDto = new TeacherDto();
        teacherDto.setId(1L);
        teacherDto.setFirstName("Alexa");
        teacherDto.setLastName("Smith");
        teacherDto.setEmail("alice@example.com");
        teacherDto.setPhone("+79001234567");
        teacherDto.setAddress("London");
        teacherDto.setGender(Gender.FEMALE);
        teacherDto.setBirthday(LocalDate.now().minusYears(30));
        teacherDto.setCathedra(cathedraDto);
        teacherDto.setSubjects(Collections.singletonList(subjectDto));

    }

    @Test
    void whenGetAll_thenReturnList() throws Exception {
        when(teacherService.findAll()).thenReturn(List.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get("/api/teachers"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].firstName").value("Alexa"));
    }

    @Test
    void whenCreate_thenReturnCreated() throws Exception {
        ObjectMapper configuredMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(teacherMapper.toEntity(any())).thenReturn(teacher);
        when(teacherService.create(any())).thenReturn(teacher);
        when(teacherMapper.toDto(any())).thenReturn(teacherDto);

        mockMvc.perform(post("/api/teachers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(configuredMapper.writeValueAsString(teacherDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.firstName").value("Alexa"));

        verify(teacherService).create(teacher);
    }

    @Test
    void whenGetById_thenReturnTeacher() throws Exception {
        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(teacherDto);

        mockMvc.perform(get("/api/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.firstName").value("Alexa"));
    }

    @Test
    void whenUpdate_thenReturnUpdated() throws Exception {
        ObjectMapper configuredMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(teacherService.existsById(1L)).thenReturn(true);
        when(teacherMapper.toEntity(any())).thenReturn(teacher);
        when(teacherService.update(any())).thenReturn(teacher);
        when(teacherMapper.toDto(any())).thenReturn(teacherDto);

        teacherDto.setEmail("Beta@mail.com");

        mockMvc.perform(put("/api/teachers/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(configuredMapper.writeValueAsString(teacherDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("Beta@mail.com"));
    }

    @Test
    void whenDelete_thenReturnNoContent() throws Exception {
        when(teacherService.existsById(1L)).thenReturn(true);
        doNothing().when(teacherService).deleteById(1L);

        mockMvc.perform(delete("/api/teachers/1"))
                .andExpect(status().isNoContent());
    }
}