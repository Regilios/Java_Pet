package org.example.univer.test.service;

import org.example.univer.dao.jdbc.JdbcTeacher;
import org.example.univer.models.Cathedra;
import org.example.univer.models.Gender;
import org.example.univer.models.Teacher;
import org.example.univer.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private JdbcTeacher mockJdbcTeacher;
    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(teacherService, "genderTeacher", "FEMALE");
    }

    @Test
    void create_teacherGenderCorrect_createTeacher() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);

        Teacher teacher = new Teacher();
        teacher.setId(3L);
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddres("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(cathedra);

        when(mockJdbcTeacher.isSingle(teacher)).thenReturn(false);
        teacherService.create(teacher);

        verify(mockJdbcTeacher, times(1)).create(teacher);
    }

    @Test
    void create_teacherGenderNotCorrect_throwException() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);

        Teacher teacher = new Teacher();
        teacher.setId(3L);
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.MALE);
        teacher.setAddres("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(cathedra);

        when(mockJdbcTeacher.isSingle(teacher)).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            teacherService.validate(teacher, TeacherService.ValidationContext.METHOD_CREATE);
            teacherService.create(teacher);
        });
        verify(mockJdbcTeacher, never()).create(any(Teacher.class));
    }

    @Test
    void isSingle_teacherIsSingle_true() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(mockJdbcTeacher.isSingle(teacher)).thenReturn(true);
        assertTrue(teacherService.isSingle(teacher));

        verify(mockJdbcTeacher, times(1)).isSingle(any(Teacher.class));
    }

    @Test
    void deleteById_deletedTeacher_deleted() {
        teacherService.deleteById(1L);

        verify(mockJdbcTeacher, times(1)).deleteById(1L);
    }

    @Test
    void findById_findStudent_found() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        when(mockJdbcTeacher.findById(1L)).thenReturn(teacher);
        Teacher result = teacherService.findById(1L);

        assertEquals(teacher, result);
    }

    @Test
    void findAll_findAllStudents_foundAll() {
        List<Teacher> teacherList = List.of(new Teacher(), new Teacher());

        when(mockJdbcTeacher.findAll()).thenReturn(teacherList);
        List<Teacher> result = teacherService.findAll();

        assertEquals(teacherList, result);
    }
}
