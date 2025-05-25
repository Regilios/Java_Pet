package org.example.univer.controllers;

import org.example.univer.dto.LectureTimeDto;
import org.example.univer.mappers.LectureTimeMapper;
import org.example.univer.models.LectureTime;
import org.example.univer.services.LectureTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
                .andExpect(model().attributeExists("lectureTimeDto"));
    }

    @Test
    void whenPostNewValidLectureTime_thenRedirectToIndex() throws Exception {
        LectureTimeDto dto = new LectureTimeDto();
        LectureTime entity = new LectureTime();
        when(lectureTimeMapper.toEntity(any(LectureTimeDto.class))).thenReturn(entity);

        mockMvc.perform(post("/lecturetimes")
                        .param("start_date", "2025-06-01")
                        .param("start_time", "10:00")
                        .param("end_date", "2025-06-01")
                        .param("end_time", "11:30"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/lecturetimes"));

        verify(lectureTimeService).create(any(LectureTime.class));
    }


    @Test
    void whenEditLectureTime_thenGroupReturnedToForm() throws Exception {
        LectureTime group = new LectureTime();
        LectureTimeDto dto = new LectureTimeDto();
        when(lectureTimeService.findById(1L)).thenReturn(Optional.of(group));
        when(lectureTimeMapper.toDto(group)).thenReturn(dto);

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
                        .param("start_date", "2025-06-01")
                        .param("start_time", "10:00")
                        .param("end_date", "2025-06-01")
                        .param("end_time", "11:30"))
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
