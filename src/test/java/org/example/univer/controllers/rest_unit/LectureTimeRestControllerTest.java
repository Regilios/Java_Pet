package org.example.univer.controllers.rest_unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.example.univer.controllers.rest.LectureTimeRestController;
import org.example.univer.dto.LectureTimeDto;
import org.example.univer.mappers.LectureTimeMapper;
import org.example.univer.models.LectureTime;
import org.example.univer.services.LectureTimeService;
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
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
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
public class LectureTimeRestControllerTest {
    @InjectMocks
    private LectureTimeRestController controller;
    @Mock
    private LectureTimeService lectureTimeService;
    @Mock
    private LectureTimeMapper lectureTimeMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private LectureTimeDto lectureTimeDto;
    private LectureTime lectureTime;
    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        // Перенастройка стандартной сериализации objectMapper с JSON т.к.
        // аннотация @JsonFormat Jackson по-прежнему требует модуль jackson-datatype-jsr310 для работы с Java 8 Date/Time API
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

        lectureTimeDto = new LectureTimeDto();
        lectureTimeDto.setId(1L);
        lectureTimeDto.setStartLecture(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        lectureTimeDto.setEndLecture(LocalDateTime.parse("2025-07-24 12:00:00", formatter));

        lectureTime = new LectureTime();
        lectureTime.setId(1L);
        lectureTime.setStartLecture(LocalDateTime.parse("2025-07-24 10:00:00", formatter));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-07-24 12:00:00", formatter));
    }

    @Test
    void whenGetAll_thenReturnList() throws Exception {
        when(lectureTimeService.findAll()).thenReturn(List.of(lectureTime));
        when(lectureTimeMapper.toDto(lectureTime)).thenReturn(lectureTimeDto);

        mockMvc.perform(get("/api/lecturetimes"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startLecture").value("2025-07-24T10:00:00"));
    }

    @Test
    void whenCreate_thenReturnCreated() throws Exception {
        when(lectureTimeMapper.toEntity(any())).thenReturn(lectureTime);
        when(lectureTimeService.create(any())).thenReturn(lectureTime);
        when(lectureTimeMapper.toDto(any())).thenReturn(lectureTimeDto);

        mockMvc.perform(post("/api/lecturetimes")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lectureTimeDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.startLecture").value("2025-07-24T10:00:00"));

        verify(lectureTimeService).create(lectureTime);
    }

    @Test
    void whenGetById_thenReturnLectureTime() throws Exception {
        when(lectureTimeService.findById(1L)).thenReturn(Optional.of(lectureTime));
        when(lectureTimeMapper.toDto(lectureTime)).thenReturn(lectureTimeDto);

        mockMvc.perform(get("/api/lecturetimes/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startLecture").value("2025-07-24T10:00:00"));
    }

    @Test
    void whenUpdate_thenReturnUpdated() throws Exception {
        when(lectureTimeService.existsById(1L)).thenReturn(true);
        when(lectureTimeMapper.toEntity(any())).thenReturn(lectureTime);
        when(lectureTimeService.update(any())).thenReturn(lectureTime);
        when(lectureTimeMapper.toDto(any())).thenReturn(lectureTimeDto);

        lectureTimeDto.setStartLecture(LocalDateTime.parse("2025-07-24 12:00:00", formatter));

        mockMvc.perform(put("/api/lecturetimes/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(lectureTimeDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startLecture").value("2025-07-24T12:00:00"));
    }

    @Test
    void whenDelete_thenReturnNoContent() throws Exception {
        when(lectureTimeService.existsById(1L)).thenReturn(true);
        doNothing().when(lectureTimeService).deleteById(1L);

        mockMvc.perform(delete("/api/lecturetimes/1"))
                .andExpect(status().isNoContent());
    }

}
