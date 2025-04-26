package test.controllers;

import org.example.univer.controllers.StudentsController;
import org.example.univer.models.Gender;
import org.example.univer.models.Group;
import org.example.univer.models.Student;
import org.example.univer.services.GroupService;
import org.example.univer.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.isA;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
public class StudentsControllerTest {
    private MockMvc mockMvc;
    @Mock
    private StudentService studentService;
    @Mock
    private GroupService groupService;
    @InjectMocks
    private StudentsController studentsController;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(studentService, "maxGroupSize", 4);
        PageableHandlerMethodArgumentResolver resolver = new PageableHandlerMethodArgumentResolver();
        resolver.setFallbackPageable(PageRequest.of(0, 1));
        mockMvc = MockMvcBuilders.standaloneSetup(studentsController).setCustomArgumentResolvers(resolver).build();
    }

    @Test
    public void whenGetAllStudents_thenAllStudentsReturned() throws Exception {
        Group group = new Group();

        Student student1 = new Student();
        student1.setFirstName("Pavel");
        student1.setLastName("Yarinov");
        student1.setGender(Gender.MALE);
        student1.setAddress("Armany str 24");
        student1.setEmail("pavel@gmail.com");
        student1.setPhone("8978474666");
        student1.setBirthday(LocalDate.of(1991, 10, 17));
        student1.setGroup(group);
        studentService.create(student1);

        Student student2 = new Student();
        student2.setFirstName("TEST");
        student2.setLastName("TEST");
        student2.setGender(Gender.MALE);
        student2.setAddress("TEST str 24");
        student2.setEmail("TEST@gmail.com");
        student2.setPhone("8978474666");
        student2.setBirthday(LocalDate.of(1991, 10, 17));
        student2.setGroup(group);
        studentService.create(student2);

        List<Student> students = Arrays.asList(student1, student2);
        Page<Student> page = new PageImpl<>(students, PageRequest.of(0, 1), 2);
        when(studentService.findAll(isA(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/students"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/index"))
                .andExpect(forwardedUrl("students/index"))
                .andExpect(model().attribute("students", page))
                .andDo(print());
        ;
    }

    @Test
    public void whenGetOneStudent_thenOneStudentReturned() throws Exception {
        Group group = new Group();

        Student student = new Student();
        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddress("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991, 10, 17));
        student.setGroup(group);
        studentService.create(student);

        when(studentService.findById(1L)).thenReturn(Optional.of(student));

        mockMvc.perform(get("/students/{id}", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("students/show"))
                .andExpect(model().attributeExists("student"))
                .andExpect(model().attribute("student", student))
                .andExpect(forwardedUrl("students/show"))
                .andDo(print());
    }

    @Test
    void whenCreateNewStudent_thenNewStudentCreated() throws Exception {
        Group group = new Group();
        group.setName("test");
        when(groupService.findAll()).thenReturn(Arrays.asList(group));

        mockMvc.perform(get("/students/new"))
                .andExpect(status().isOk())
                .andExpect(view().name("students/new"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attributeExists("student"))
                .andDo(print());

        verify(groupService, times(1)).findAll();
    }

    @Test
    void whenEditStudent_thenStudentFound() throws Exception {
        Group group = new Group();

        Student student = new Student();
        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddress("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991, 10, 17));
        student.setGroup(group);

        List<Group> groupList = List.of(group);

        when(studentService.findById(1L)).thenReturn(Optional.of(student));
        when(groupService.findAll()).thenReturn(groupList);

        mockMvc.perform(get("/students/{id}/edit", 1))
                .andExpect(status().isOk())
                .andExpect(view().name("students/edit"))
                .andExpect(model().attributeExists("groups"))
                .andExpect(model().attributeExists("student"))
                .andExpect(forwardedUrl("students/edit"))
                .andExpect(model().attribute("groups", groupList))
                .andExpect(model().attribute("student", student))
                .andDo(print());

        verify(studentService, times(1)).findById(1L);
        verify(groupService, times(1)).findAll();
    }

    @Test
    public void whenUpdateStudent_thenStudentUpdated() throws Exception {
        Group group = new Group();

        Student student = new Student();
        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddress("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991, 10, 17));
        student.setGroup(group);

        mockMvc.perform(patch("/students/{id}", 1)
                        .flashAttr("student", student))
                .andExpect(status().is3xxRedirection())
                .andExpect(redirectedUrl("/students"))
                .andDo(print());

        verify(studentService, times(1)).update(student);
    }

    @Test
    void whenDeleteStudent_thenStudentDeleted() throws Exception {
        mockMvc.perform(delete("/students/{id}", 1))
                .andExpect(redirectedUrl("/students"));

        ArgumentCaptor<Student> studentArgumentCaptor = ArgumentCaptor.forClass(Student.class);
        verify(studentService).deleteById(studentArgumentCaptor.capture());

        Student actual = studentArgumentCaptor.getValue();
        assertEquals(1L, actual.getId());
    }

}
