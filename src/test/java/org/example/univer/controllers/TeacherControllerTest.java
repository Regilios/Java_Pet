package org.example.univer.controllers;

import org.example.univer.controllers.web.TeacherController;
import org.example.univer.dto.TeacherDto;
import org.example.univer.mappers.TeacherMapper;
import org.example.univer.models.Teacher;
import org.example.univer.services.CathedraService;
import org.example.univer.services.SubjectService;
import org.example.univer.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {
    private MockMvc mockMvc;
    private TeacherService teacherService;
    private TeacherController teacherController;
    private TeacherMapper teacherMapper;
    private SubjectService subjectService;
    private CathedraService cathedraService;

    @BeforeEach
    public void setUp() {
        teacherService = mock(TeacherService.class);
        teacherMapper = mock(TeacherMapper.class);
        cathedraService =  mock(CathedraService.class);
        subjectService =  mock(SubjectService.class);
        teacherController = new TeacherController(
                 teacherService,
                 cathedraService,
                 subjectService,
                 teacherMapper);
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
    }

    @Test
    void whenFindAll_thenAllTeachersReturned() throws Exception {
        Teacher group = new Teacher();
        TeacherDto dto = new TeacherDto();
        when(teacherService.findAll()).thenReturn(List.of(group));
        when(teacherMapper.toDto(group)).thenReturn(dto);

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/index"))
                .andExpect(model().attributeExists("teachersDto"))
                .andExpect(model().attribute("title", "All Teachers"));
    }

    @Test
    void whenGetCreateForm_thenEmptyFormReturned() throws Exception {
        mockMvc.perform(get("/teachers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/new"))
                .andExpect(model().attributeExists("teacherDto"));
    }

    @Test
    void whenPostNewValidTeacher_thenRedirectToIndex() throws Exception {
        TeacherDto dto = new TeacherDto();
        Teacher entity = new Teacher();
        when(teacherMapper.toEntity(any(TeacherDto.class))).thenReturn(entity);

        mockMvc.perform(post("/teachers")
                        .param("subjectIds","1","2")
                        .param("cathedra.id","1")
                        .param("firstName","test")
                        .param("lastName","test")
                        .param("address","test")
                        .param("phone","+79147521710")
                        .param("email","ttt@ttt.ru")
                        .param("birthday","2005-10-20")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService).create(any(Teacher.class));
    }

    @Test
    void whenEditGroup_thenTeacherReturnedToForm() throws Exception {
        Teacher teacher = new Teacher();
        TeacherDto dto = new TeacherDto();
        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(dto);

        mockMvc.perform(get("/teachers/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/edit"))
                .andExpect(model().attributeExists("teacherDto"));
    }

    @Test
    void whenUpdateValidTeacher_thenRedirectToIndex() throws Exception {
        TeacherDto dto = new TeacherDto();
        Teacher entity = new Teacher();
        when(teacherMapper.toEntity(any(TeacherDto.class))).thenReturn(entity);

        mockMvc.perform(patch("/teachers/1")
                        .param("subjectIds","1","2")
                        .param("cathedra.id","1")
                        .param("firstName","test")
                        .param("lastName","test")
                        .param("address","test")
                        .param("phone","+79147521710")
                        .param("email","ttt@ttt.ru")
                        .param("birthday","2005-10-20")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService).update(any(Teacher.class));
    }

    @Test
    void whenFindById_thenTeacherReturned() throws Exception {
        Teacher teacher = new Teacher();
        TeacherDto dto = new TeacherDto();
        when(teacherService.findById(1L)).thenReturn(Optional.of(teacher));
        when(teacherMapper.toDto(teacher)).thenReturn(dto);

        mockMvc.perform(get("/teachers/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/show"))
                .andExpect(model().attributeExists("teacherDto"));
    }

    @Test
    void whenDeleteTeacher_thenRedirectToIndex() throws Exception {
        mockMvc.perform(delete("/teachers/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService).deleteById(1L);
    }
}
