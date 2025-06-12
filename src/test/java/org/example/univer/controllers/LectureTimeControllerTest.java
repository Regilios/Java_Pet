package org.example.univer.controllers;

import org.example.univer.controllers.web.LectureTimeController;
import org.example.univer.dto.LectureTimeDto;
import org.example.univer.dto.LectureTimeFormDto;
import org.example.univer.mappers.LectureTimeMapper;
import org.example.univer.models.LectureTime;
import org.example.univer.services.LectureTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class LectureTimeControllerTest {
    private MockMvc mockMvc;
    private LectureTimeService lectureTimeService;
    private LectureTimeMapper lectureTimeMapper;
    private LectureTimeController lectureTimeController;

    @BeforeEach
    void setUp() {
        lectureTimeService = mock(LectureTimeService.class);
        lectureTimeMapper = mock(LectureTimeMapper.class);
        lectureTimeController = new LectureTimeController(lectureTimeService, lectureTimeMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(lectureTimeController).build();
    }


    @Test
    void whenFindAll_thenAllLectureTimeReturned() throws Exception {
        LectureTime lectureTime = new LectureTime();
        LectureTimeDto dto = new LectureTimeDto();
        when(lectureTimeService.findAll()).thenReturn(List.of(lectureTime));
        when(lectureTimeMapper.toDto(lectureTime)).thenReturn(dto);

        mockMvc.perform(get("/lecturetimes"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturetimes/index"))
                .andExpect(model().attributeExists("lectureTimesDto"))
                .andExpect(model().attribute("title", "All lecturetimes"));
    }

    @Test
    void whenGetCreateForm_thenEmptyFormReturned() throws Exception {
        mockMvc.perform(get("/lecturetimes/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturetimes/new"))
                .andExpect(model().attributeExists("lectureTimeFormDto"));
    }

    @Test
    void whenPostNewValidLectureTime_thenRedirectToIndex() throws Exception {
        LectureTimeFormDto dto = new LectureTimeFormDto();
        LectureTime entity = new LectureTime();
        when(lectureTimeMapper.toEntity(any(LectureTimeDto.class))).thenReturn(entity);

        mockMvc.perform(post("/lecturetimes")
                        .param("startDate", "2025-06-01")
                        .param("startTime", "10:00")
                        .param("endDate", "2025-06-01")
                        .param("endTime", "11:30")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturetimes"));

        verify(lectureTimeService).create(any(LectureTime.class));
    }


    @Test
    void whenEditLectureTime_thenGroupReturnedToForm() throws Exception {
        LocalDateTime start = LocalDateTime.of(2025, 6, 1, 10, 0);
        LocalDateTime end = LocalDateTime.of(2025, 6, 1, 12, 0);

        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        lectureTime.setStartLecture(start);
        lectureTime.setEndLecture(end);

        LectureTimeDto dto = new LectureTimeDto();
        dto.setId(1L);
        dto.setStartLecture(start);
        dto.setEndLecture(end);

        when(lectureTimeService.findById(1L)).thenReturn(Optional.of(lectureTime));
        when(lectureTimeMapper.toDto(lectureTime)).thenReturn(dto);

        mockMvc.perform(get("/lecturetimes/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturetimes/edit"))
                .andExpect(model().attributeExists("lectureTimeDto"));
    }

    @Test
    void whenUpdateValidLectureTime_thenRedirectToIndex() throws Exception {
        LectureTimeDto dto = new LectureTimeDto();
        LectureTime entity = new LectureTime();
        when(lectureTimeMapper.toEntity(any(LectureTimeDto.class))).thenReturn(entity);

        mockMvc.perform(patch("/lecturetimes/1")
                        .param("startDate", "2025-06-01")
                        .param("startTime", "10:00")
                        .param("endDate", "2025-06-01")
                        .param("endTime", "11:30")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturetimes"));

        verify(lectureTimeService).update(any(LectureTime.class));
    }

    @Test
    void whenFindById_thenLectureTimeReturned() throws Exception {
        LectureTime group = new LectureTime();
        LectureTimeDto dto = new LectureTimeDto();
        when(lectureTimeService.findById(1L)).thenReturn(Optional.of(group));
        when(lectureTimeMapper.toDto(group)).thenReturn(dto);

        mockMvc.perform(get("/lecturetimes/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("lecturetimes/show"))
                .andExpect(model().attributeExists("lectureTimeDto"));
    }

    @Test
    void whenDeleteLectureTime_thenRedirectToIndex() throws Exception {
        mockMvc.perform(delete("/lecturetimes/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturetimes"));

        verify(lectureTimeService).deleteById(1L);
    }
}
