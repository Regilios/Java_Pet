package org.example.univer.controllers.rest_unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.example.univer.controllers.rest.StudentRestController;
import org.example.univer.dto.CathedraDto;
import org.example.univer.dto.GroupDto;
import org.example.univer.dto.StudentDto;
import org.example.univer.mappers.StudentMapper;
import org.example.univer.models.Cathedra;
import org.example.univer.models.Gender;
import org.example.univer.models.Group;
import org.example.univer.models.Student;
import org.example.univer.services.StudentService;
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
public class StudentRestControllerTest {
    @InjectMocks
    private StudentRestController controller;
    @Mock
    private StudentService studentService;
    @Mock
    private StudentMapper studentMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private StudentDto studentDto;
    private Student student;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        CathedraDto cathedraDto = new CathedraDto();
        cathedraDto.setId(1L);
        cathedraDto.setName("Math");

        GroupDto groupDto = new GroupDto();
        groupDto.setId(1L);
        groupDto.setName("Alpha");
        groupDto.setCathedra(cathedraDto);

        studentDto = new StudentDto();
        studentDto.setId(1L);
        studentDto.setGroup(groupDto);
        studentDto.setGender(Gender.MALE);
        studentDto.setAddress("London");
        studentDto.setEmail("aaaa@mail.ru");
        studentDto.setPhone("+79001234567");
        studentDto.setBirthday(LocalDate.parse("1991-12-10"));
        studentDto.setFirstName("Igor");
        studentDto.setLastName("Paragvaev");

        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        cathedra.setName("Math");

        Group group = new Group();
        group.setId(1L);
        group.setName("Alpha");
        group.setCathedra(cathedra);

        student = new Student();
        student.setId(1L);
        student.setGroup(group);
        student.setGender(Gender.MALE);
        student.setAddress("London");
        student.setEmail("aaaa@mail.ru");
        student.setPhone("+79001234567");
        student.setBirthday(LocalDate.parse("1991-12-10"));
        student.setFirstName("Igor");
        student.setLastName("Paragvaev");
    }

    @Test
    void whenGetAll_thenReturnList() throws Exception {
        when(studentService.findAll()).thenReturn(List.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        mockMvc.perform(get("/api/students"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].address").value("London"));
    }

    @Test
    void whenCreate_thenReturnCreated() throws Exception {
        ObjectMapper configuredMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(studentMapper.toEntity(any())).thenReturn(student);
        when(studentService.create(any())).thenReturn(student);
        when(studentMapper.toDto(any())).thenReturn(studentDto);

        mockMvc.perform(post("/api/students")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(configuredMapper.writeValueAsString(studentDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.address").value("London"));

        verify(studentService).create(student);
    }

    @Test
    void whenGetById_thenReturnStudent() throws Exception {
        when(studentService.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(studentDto);

        mockMvc.perform(get("/api/students/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("London"));
    }

    @Test
    void whenUpdate_thenReturnUpdated() throws Exception {
        ObjectMapper configuredMapper = new ObjectMapper()
                .registerModule(new JavaTimeModule())
                .disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        when(studentService.existsById(1L)).thenReturn(true);
        when(studentMapper.toEntity(any())).thenReturn(student);
        when(studentService.update(any())).thenReturn(student);
        when(studentMapper.toDto(any())).thenReturn(studentDto);

        studentDto.setEmail("Beta@mail.com");

        mockMvc.perform(put("/api/students/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(configuredMapper.writeValueAsString(studentDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("Beta@mail.com"));
    }

    @Test
    void whenDelete_thenReturnNoContent() throws Exception {
        when(studentService.existsById(1L)).thenReturn(true);
        doNothing().when(studentService).deleteById(1L);

        mockMvc.perform(delete("/api/students/1"))
                .andExpect(status().isNoContent());
    }
}
