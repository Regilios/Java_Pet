package org.example.univer.controllers;

import org.example.univer.controllers.web.StudentController;
import org.example.univer.dto.StudentDto;
import org.example.univer.mappers.StudentMapper;
import org.example.univer.models.Student;
import org.example.univer.services.GroupService;
import org.example.univer.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StudentsControllerTest {
    private MockMvc mockMvc;
    @Mock
    private GroupService groupService;
    @Mock
    private StudentService studentService;
    @Mock
    private StudentMapper studentMapper;
    @InjectMocks
    private StudentController studentController;

    @BeforeEach
    public void setUp() {
        PageableHandlerMethodArgumentResolver pageableResolver = new PageableHandlerMethodArgumentResolver();
        pageableResolver.setFallbackPageable(PageRequest.of(0, 10));
        mockMvc = MockMvcBuilders
                .standaloneSetup(studentController)
                .setCustomArgumentResolvers(pageableResolver)
                .build();
    }

    @Test
    void whenFindAllStudents_thenAllStudentsReturned() throws Exception {
        Student student = new Student();
        StudentDto dto = new StudentDto();

        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> studentPage = new PageImpl<>(List.of(student), pageable, 1);

        when(studentService.findAll(any(Pageable.class))).thenReturn(studentPage);
        when(studentMapper.toDto(student)).thenReturn(dto);

        mockMvc.perform(get("/students").param("page", "0").param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/index"))
                .andExpect(model().attributeExists("studentsDto"))
                .andExpect(model().attribute("title", "All Students"));
    }

    @Test
    void whenGetCreateForm_thenEmptyFormReturned() throws Exception {
        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/new"))
                .andExpect(model().attributeExists("studentDto"));

    }

    @Test
    void whenPostNewValidAStudent_thenRedirectToIndex() throws Exception {
        StudentDto dto = new StudentDto();
        Student entity = new Student();
        when(studentMapper.toEntity(any())).thenReturn(entity);

        mockMvc.perform(post("/students")
                        .param("group.id","1")
                        .param("firstName","test")
                        .param("lastName","test")
                        .param("address","test")
                        .param("phone","+79147521710")
                        .param("email","ttt@ttt.ru")
                        .param("birthday","2005-10-20")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService).create(any(Student.class));
    }

    @Test
    void whenEditStudent_thenStudentReturnedToForm() throws Exception {
        Student student = new Student();
        StudentDto dto = new StudentDto();
        when(studentService.findById(1L)).thenReturn(Optional.of(student));
        when(studentMapper.toDto(student)).thenReturn(dto);

        mockMvc.perform(get("/students/1/edit"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/edit"))
                .andExpect(model().attributeExists("studentDto"));
    }

    @Test
    void whenUpdateValidStudent_thenRedirectToIndex() throws Exception {
        StudentDto dto = new StudentDto();
        Student entity = new Student();
        when(studentMapper.toEntity(any())).thenReturn(entity);

        mockMvc.perform(patch("/students/1")
                        .param("group.id","1")
                        .param("firstName","test")
                        .param("lastName","test")
                        .param("address","test")
                        .param("phone","+79147521710")
                        .param("email","ttt@ttt.ru")
                        .param("birthday","2005-10-20")
                        .contentType(MediaType.APPLICATION_FORM_URLENCODED))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService).update(any(Student.class));
    }

    @Test
    void whenFindById_thenStudentReturned() throws Exception {
        Student audience = new Student();
        StudentDto dto = new StudentDto();
        when(studentService.findById(1L)).thenReturn(Optional.of(audience));
        when(studentMapper.toDto(audience)).thenReturn(dto);

        mockMvc.perform(get("/students/1"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/show"))
                .andExpect(model().attributeExists("studentDto"));
    }

    @Test
    void whenDeleteStudent_thenRedirectToIndex() throws Exception {
        mockMvc.perform(delete("/students/1"))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"));

        verify(studentService).deleteById(1L);
    }
}
