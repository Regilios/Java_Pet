package org.example.univer.test.controllers;

import org.example.univer.controllers.TeacherController;
import org.example.univer.models.*;
import org.example.univer.services.CathedraService;
import org.example.univer.services.SubjectService;
import org.example.univer.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.servlet.mvc.support.RedirectAttributesModelMap;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class TeacherControllerTest {
    private MockMvc mockMvc;
    @Mock
    private TeacherService teacherService;
    @Mock
    private SubjectService subjectService;
    @Mock
    private CathedraService cathedraService;
    @InjectMocks
    private TeacherController teacherController;

    @BeforeEach
    public void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(teacherController).build();
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(teacherService, "genderTeacher", "FEMALE");
    }


    @Test
    public void whenGetAllTeachers_thenAllTeachersReturned() throws Exception {
        Teacher teacher1 = new Teacher();
        teacher1.setFirstName("test");
        teacher1.setLastName("test2");
        teacher1.setGender(Gender.FEMALE);
        teacher1.setAddress("test");
        teacher1.setEmail("test@test");
        teacher1.setPhone("test");
        teacher1.setBirthday(LocalDate.parse("1983-02-01"));

        Teacher teacher2 = new Teacher();
        teacher2.setFirstName("test2");
        teacher2.setLastName("test22");
        teacher2.setGender(Gender.FEMALE);
        teacher2.setAddress("test2");
        teacher2.setEmail("test@test");
        teacher2.setPhone("test");
        teacher2.setBirthday(LocalDate.parse("1983-02-01"));

        List<Teacher> teachers = Arrays.asList(teacher1, teacher2);

        when(teacherService.findAll()).thenReturn(teachers);

        mockMvc.perform(get("/teachers"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/index"))
                .andExpect(forwardedUrl("teachers/index"))
                .andExpect(model().attribute("teachers", teachers))
                .andDo(print());
    }

    @Test
    public void whenGetOneTeacher_thenOneTeacherReturned() throws Exception {
        Cathedra cathedra = new Cathedra();

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(cathedra);

        teacherService.create(teacher);

        when(teacherService.findById(1L)).thenReturn(teacher);

        mockMvc.perform(get("/teachers/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/show"))
                .andExpect(model().attributeExists("teacher"))
                .andExpect(model().attribute("teacher", teacher))
                .andExpect(forwardedUrl("teachers/show"))
                .andDo(print());
    }

    @Test
    void whenCreateNewTeacher_thenNewTeacherCreated() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");

        Subject subject = new Subject();
        subject.setName("test");

        when(cathedraService.findAll()).thenReturn(Arrays.asList(cathedra));
        when(subjectService.findAll()).thenReturn(Arrays.asList(subject));

        mockMvc.perform(get("/teachers/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/new"))
                .andExpect(model().attributeExists("cathedras"))
                .andExpect(model().attributeExists("subjects"))
                .andExpect(model().attributeExists("teacher"))
                .andDo(print());

        verify(cathedraService, times(1)).findAll();
        verify(subjectService, times(1)).findAll();
    }

    @Test
    void whenEditTeacher_thenTeacherFound() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");

        Subject subject = new Subject();
        subject.setName("test");

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(cathedra);

        List<Cathedra> cathedrasList = List.of(cathedra);
        List<Subject> subjectList = List.of(subject);

        when(teacherService.findById(1L)).thenReturn(teacher);
        when(cathedraService.findAll()).thenReturn(cathedrasList);
        when(subjectService.findAll()).thenReturn(subjectList);

        mockMvc.perform(get("/teachers/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("teachers/edit"))
                .andExpect(model().attributeExists("cathedras"))
                .andExpect(model().attributeExists("subjects"))
                .andExpect(model().attributeExists("teacher"))
                .andExpect(forwardedUrl("teachers/edit"))
                .andExpect(model().attribute("cathedras", cathedrasList))
                .andExpect(model().attribute("subjects", subjectList))
                .andExpect(model().attribute("teacher", teacher))
                .andDo(print());

        verify(teacherService, times(1)).findById(1L);
        verify(cathedraService, times(1)).findAll();
        verify(subjectService, times(1)).findAll();
    }

    @Test
    public void whenUpdateTeacher_thenTeacherUpdated() throws Exception {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");

        Subject subject1 = new Subject();
        subject1.setName("test");
        Subject subject2 = new Subject();
        subject2.setName("test2");

        List<Long> subjects = Arrays.asList(subject1.getId(), subject2.getId());

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(cathedra);

        mockMvc.perform(patch("/teachers/{id}", 1)
                        .flashAttr("teacher", teacher)
                        .param("subjects", "1", "2")
                        .sessionAttr("model", new RedirectAttributesModelMap()))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/teachers"))
                .andDo(print());

        verify(teacherService, times(1)).update(teacher);
    }

    @Test
    void whenDeleteTeacher_thenTeacherDeleted() throws Exception {
        mockMvc.perform(delete("/teachers/{id}", 1))
                .andExpect(redirectedUrl("/teachers"));

        verify(teacherService).deleteById(1L);
    }
}
