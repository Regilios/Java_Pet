package org.example.univer.controllers.rest_unit;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import com.fasterxml.jackson.datatype.jsr310.deser.LocalDateDeserializer;
import com.fasterxml.jackson.datatype.jsr310.ser.LocalDateSerializer;
import org.example.univer.controllers.rest.HolidayRestController;
import org.example.univer.dto.HolidayDto;
import org.example.univer.mappers.HolidayMapper;
import org.example.univer.models.Holiday;
import org.example.univer.services.HolidayService;
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
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc
public class HolidayRestControllerTest {
    @InjectMocks
    private HolidayRestController controller;
    @Mock
    private HolidayService holidayService;
    @Mock
    private HolidayMapper holidayMapper;
    private MockMvc mockMvc;
    private ObjectMapper objectMapper = new ObjectMapper();
    private HolidayDto holidayDto;
    private Holiday holiday;


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

        holidayDto = new HolidayDto();
        holidayDto.setId(1L);
        holidayDto.setStartHoliday(LocalDate.parse("2025-10-13"));
        holidayDto.setEndHoliday(LocalDate.parse("2025-10-20"));
        holidayDto.setDescription("test");

        holiday = new Holiday();
        holiday.setId(1L);
        holiday.setStartHoliday(LocalDate.parse("2025-10-13"));
        holiday.setEndHoliday(LocalDate.parse("2025-10-20"));
        holiday.setDescription("test");
    }

    @Test
    void whenGetAll_thenReturnList() throws Exception {
        when(holidayService.findAll()).thenReturn(List.of(holiday));
        when(holidayMapper.toDto(holiday)).thenReturn(holidayDto);

        mockMvc.perform(get("/api/holidays"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].startHoliday").value("2025-10-13"));
    }

    @Test
    void whenCreate_thenReturnCreated() throws Exception {
        when(holidayMapper.toEntity(any())).thenReturn(holiday);
        when(holidayService.create(any())).thenReturn(holiday);
        when(holidayMapper.toDto(any())).thenReturn(holidayDto);

        mockMvc.perform(post("/api/holidays")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(holidayDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.startHoliday").value("2025-10-13"));

        verify(holidayService).create(holiday);
    }

    @Test
    void whenGetById_thenReturnHoliday() throws Exception {
        when(holidayService.findById(1L)).thenReturn(Optional.of(holiday));
        when(holidayMapper.toDto(holiday)).thenReturn(holidayDto);

        mockMvc.perform(get("/api/holidays/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.startHoliday").value("2025-10-13"));
    }

    @Test
    void whenUpdate_thenReturnUpdated() throws Exception {
        when(holidayService.existsById(1L)).thenReturn(true);
        when(holidayMapper.toEntity(any())).thenReturn(holiday);
        when(holidayService.update(any())).thenReturn(holiday);
        when(holidayMapper.toDto(any())).thenReturn(holidayDto);

        holidayDto.setDescription("Beta");

        mockMvc.perform(put("/api/holidays/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(holidayDto)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Beta"));
    }

    @Test
    void whenDelete_thenReturnNoContent() throws Exception {
        when(holidayService.existsById(1L)).thenReturn(true);
        doNothing().when(holidayService).deleteById(1L);

        mockMvc.perform(delete("/api/holidays/1"))
                .andExpect(status().isNoContent());
    }


}
