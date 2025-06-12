package org.example.univer.controllers;

import org.example.univer.controllers.web.SubjectController;
import org.example.univer.dto.SubjectDto;
import org.example.univer.mappers.SubjectMapper;
import org.example.univer.models.Subject;
import org.example.univer.services.SubjectService;
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

public class SubjectControllerTest {
    private SubjectService subjectService;
    private SubjectMapper subjectMapper;
    private SubjectController subjectController;
    private MockMvc mockMvc;

    @BeforeEach
    void setUp() {
        subjectService = mock(SubjectService.class);
        subjectMapper = mock(SubjectMapper.class);
        subjectController = new SubjectController(subjectService, subjectMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(subjectController).build();
    }

    @Test
    void whenFindAll_thenAllSubjectsReturned() throws Exception {
        Subject subject = new Subject();
        SubjectDto dto = new SubjectDto();
        when(subjectService.findAll()).thenReturn(List.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(dto);

        mockMvc.perform(get("/subjects"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/index"))
                .andExpect(model().attributeExists("subjectsDto"))
                .andExpect(model().attribute("title", "All Subjects"));
    }

    @Test
    void whenGetCreateForm_thenEmptyFormReturned() throws Exception {
        mockMvc.perform(get("/subjects/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/new"))
                .andExpect(model().attributeExists("subjectDto"));
    }

    @Test
    void whenPostNewValidSubject_thenRedirectToIndex() throws Exception {
        SubjectDto dto = new SubjectDto();
        Subject entity = new Subject();
        when(subjectMapper.toEntity(any(SubjectDto.class))).thenReturn(entity);

        mockMvc.perform(post("/subjects")
                        .param("name", "Math")
                        .param("description", "Algebra and Geometry")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects"));

        verify(subjectService).create(any(Subject.class));
    }

    @Test
    void whenEditSubject_thenSubjectReturnedToForm() throws Exception {
        Subject subject = new Subject();
        SubjectDto dto = new SubjectDto();
        when(subjectService.findById(1L)).thenReturn(Optional.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(dto);

        mockMvc.perform(get("/subjects/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/edit"))
                .andExpect(model().attributeExists("subjectDto"));
    }

    @Test
    void whenUpdateValidSubject_thenRedirectToIndex() throws Exception {
        SubjectDto dto = new SubjectDto();
        Subject entity = new Subject();
        when(subjectMapper.toEntity(any(SubjectDto.class))).thenReturn(entity);

        mockMvc.perform(patch("/subjects/1")
                        .param("name", "Physics")
                        .param("description", "Algebra and Geometry")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects"));

        verify(subjectService).update(any(Subject.class));
    }

    @Test
    void whenFindById_thenSubjectReturned() throws Exception {
        Subject subject = new Subject();
        SubjectDto dto = new SubjectDto();
        when(subjectService.findById(1L)).thenReturn(Optional.of(subject));
        when(subjectMapper.toDto(subject)).thenReturn(dto);

        mockMvc.perform(get("/subjects/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("subjects/show"))
                .andExpect(model().attributeExists("subjectDto"));
    }

    @Test
    void whenDeleteSubject_thenRedirectToIndex() throws Exception {
        mockMvc.perform(delete("/subjects/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/subjects"));

        verify(subjectService).deleteById(1L);
    }
}
