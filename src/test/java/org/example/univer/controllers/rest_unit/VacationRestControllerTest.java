package org.example.univer.controllers.rest_unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.example.univer.controllers.rest.VacationRestController;
import org.example.univer.dto.VacationDto;
import org.example.univer.mappers.VacationMapper;
import org.example.univer.models.*;
import org.example.univer.services.VacationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
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
public class VacationRestControllerTest {
    @InjectMocks
    private VacationRestController controller;
    @Mock
    private VacationService vacationService;
    @Mock
    private VacationMapper vacationMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private VacationDto vacationDto;
    private Vacation vacation;

    @BeforeEach
    void setUp() {
        objectMapper = new ObjectMapper();
        JavaTimeModule javaTimeModule = new JavaTimeModule();
        javaTimeModule.addSerializer(LocalDate.class, new LocalDateSerializer(DateTimeFormatter.ISO_DATE));
        javaTimeModule.addDeserializer(LocalDate.class, new LocalDateDeserializer(DateTimeFormatter.ISO_DATE));

        objectMapper.registerModule(javaTimeModule);
        objectMapper.disable(SerializationFeature.WRITE_DATES_AS_TIMESTAMPS);

        mockMvc = MockMvcBuilders.standaloneSetup(controller)
                .setControllerAdvice()
                .setMessageConverters(new MappingJackson2HttpMessageConverter(objectMapper))
                .build();

        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        cathedra.setName("Math");

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("Test");
        subject.setDescription("test test test test test test");

        Teacher teacher = new Teacher();
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

        vacationDto = new VacationDto();
        vacationDto.setId(1L);
        vacationDto.setStartJob(LocalDate.parse("2025-10-01"));
        vacationDto.setEndJob(LocalDate.parse("2025-10-11"));
        vacationDto.setTeacherId(1L);
        vacationDto.setTeacherLastName("Alex");
        vacationDto.setTeacherLastName("Smith");

        vacation = new Vacation();
        vacation.setId(1L);
        vacation.setStartJob(LocalDate.parse("2025-10-01"));
        vacation.setEndJob(LocalDate.parse("2025-10-11"));
        vacation.setTeacher(teacher);
    }

    @Test
    void whenGetAll_thenReturnList() throws Exception {
        when(vacationService.findAll()).thenReturn(List.of(vacation));
        when(vacationMapper.toDto(vacation)).thenReturn(vacationDto);

        mockMvc.perform(get("/api/vacations"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startJob").value("2025-10-01"));
    }

    @Test
    void whenCreate_thenReturnCreated() throws Exception {
        when(vacationMapper.toEntity(any())).thenReturn(vacation);
        when(vacationService.create(any())).thenReturn(vacation);
        when(vacationMapper.toDto(any())).thenReturn(vacationDto);

        mockMvc.perform(post("/api/vacations")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vacationDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.startJob").value("2025-10-01"));

        verify(vacationService).create(vacation);
    }

    @Test
    void whenGetById_thenReturnVacation() throws Exception {
        when(vacationService.findById(1L)).thenReturn(Optional.of(vacation));
        when(vacationMapper.toDto(vacation)).thenReturn(vacationDto);

        mockMvc.perform(get("/api/vacations/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.endJob").value("2025-10-11"));
    }

    @Test
    void whenUpdate_thenReturnUpdated() throws Exception {
        when(vacationService.existsById(1L)).thenReturn(true);
        when(vacationMapper.toEntity(any())).thenReturn(vacation);
        when(vacationService.update(any())).thenReturn(vacation);
        when(vacationMapper.toDto(any())).thenReturn(vacationDto);

        vacationDto.setTeacherFirstName("Olga");

        mockMvc.perform(put("/api/vacations/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(vacationDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.teacherFirstName").value("Olga"));
    }

    @Test
    void whenDelete_thenReturnNoContent() throws Exception {
        when(vacationService.existsById(1L)).thenReturn(true);
        doNothing().when(vacationService).deleteById(1L);

        mockMvc.perform(delete("/api/vacations/1"))
                .andExpect(status().isNoContent());
    }
}
