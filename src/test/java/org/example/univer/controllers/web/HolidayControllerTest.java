package org.example.univer.controllers.web;

import org.example.univer.dto.HolidayDto;
import org.example.univer.mappers.HolidayMapper;
import org.example.univer.models.Holiday;
import org.example.univer.services.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class HolidayControllerTest {
    private MockMvc mockMvc;
    private HolidayService holidayService;
    private HolidayController holidayController;
    private HolidayMapper holidayMapper;

    @BeforeEach
    void setUp() {
        holidayService = mock(HolidayService.class);
        holidayMapper = mock(HolidayMapper.class);
        holidayController = new HolidayController(holidayService, holidayMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(holidayController).build();
    }

    @Test
    void whenFindAll_thenAllHolidaysReturned() throws Exception {
        Holiday holiday = new Holiday();
        HolidayDto dto = new HolidayDto();
        when(holidayService.findAll()).thenReturn(List.of(holiday));
        when(holidayMapper.toDto(holiday)).thenReturn(dto);

        mockMvc.perform(get("/holidays"))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/index"))
                .andExpect(model().attributeExists("holidaysDto"))
                .andExpect(model().attribute("title", "All Holidays"));
    }

    @Test
    void whenGetCreateForm_thenEmptyFormReturned() throws Exception {
        mockMvc.perform(get("/holidays/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/new"))
                .andExpect(model().attributeExists("holidayDto"));
    }


    @Test
    void whenPostNewValidHoliday_thenRedirectToIndex() throws Exception {
        HolidayDto dto = new HolidayDto();
        Holiday entity = new Holiday();
        when(holidayMapper.toEntity(any(HolidayDto.class))).thenReturn(entity);

        mockMvc.perform(post("/holidays")
                        .param("description","test test test test")
                        .param("startHoliday","2025-10-10")
                        .param("endHoliday","2025-10-20")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/holidays"));

        verify(holidayService).create(any(Holiday.class));
    }


    @Test
    void whenEditHoliday_thenHolidayReturnedToForm() throws Exception {
        Holiday group = new Holiday();
        HolidayDto dto = new HolidayDto();
        when(holidayService.findById(1L)).thenReturn(Optional.of(group));
        when(holidayMapper.toDto(group)).thenReturn(dto);

        mockMvc.perform(get("/holidays/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/edit"))
                .andExpect(model().attributeExists("holidayDto"));
    }

    @Test
    void whenUpdateValidHoliday_thenRedirectToIndex() throws Exception {
        HolidayDto dto = new HolidayDto();
        Holiday entity = new Holiday();
        when(holidayMapper.toEntity(any(HolidayDto.class))).thenReturn(entity);

        mockMvc.perform(patch("/holidays/1")
                .param("description","test test test test")
                .param("startHoliday","2025-10-10")
                .param("endHoliday","2025-10-20")
                .contentType(MediaType.APPLICATION_FORM_URLENCODED))
            .andExpect(status().is3xxRedirection())
            .andExpect(redirectedUrl("/holidays"));

        verify(holidayService).update(any(Holiday.class));
    }

    @Test
    void whenFindById_thenHolidayReturned() throws Exception {
        Holiday holiday = new Holiday();
        HolidayDto dto = new HolidayDto();
        when(holidayService.findById(1L)).thenReturn(Optional.of(holiday));
        when(holidayMapper.toDto(holiday)).thenReturn(dto);

        mockMvc.perform(get("/holidays/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("holidays/show"))
                .andExpect(model().attributeExists("holidayDto"));
    }

    @Test
    void whenDeleteHoliday_thenRedirectToIndex() throws Exception {
        mockMvc.perform(delete("/holidays/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/holidays"));

        verify(holidayService).deleteById(1L);
    }
}
