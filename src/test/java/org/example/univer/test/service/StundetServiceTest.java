package org.example.univer.test.service;

import org.example.univer.dao.jdbc.JdbcStudent;
import org.example.univer.exeption.StudentExeption;
import org.example.univer.models.Gender;
import org.example.univer.models.Group;
import org.example.univer.models.Student;
import org.example.univer.services.StudentService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class StundetServiceTest {
    @Mock
    private JdbcStudent mockJdbcStudent;
    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(studentService, "maxGroupSize", 4);
    }

    @Test
    void create_studentThereIsFreePlaceInGroup_createStudent() {
        Group group = new Group();

        Student student = new Student();
        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddress("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991,10,17));
        student.setGroup(group);

        when(mockJdbcStudent.isSingle(student)).thenReturn(false);
        when(mockJdbcStudent.checkGroupSize(student)).thenReturn(3);
        studentService.create(student);

        verify(mockJdbcStudent, times(1)).create(student);
    }

    @Test
    void create_studentThereIsNotFreePlaceInGroup_throwException() {
        Group group = new Group();

        Student student = new Student();
        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddress("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991,10,17));
        student.setGroup(group);

        when(mockJdbcStudent.isSingle(student)).thenReturn(false);
        when(mockJdbcStudent.checkGroupSize(student)).thenReturn(6);

        assertThrows(StudentExeption.class, () -> {
            studentService.validate(student, StudentService.ValidationContext.METHOD_CREATE);
            studentService.create(student);
        });
        verify(mockJdbcStudent, never()).create(any(Student.class));
    }

    @Test
    void isSingle_studentIsSingle_true() {
        Student student = new Student();

        when(mockJdbcStudent.isSingle(student)).thenReturn(true);
        assertTrue(studentService.isSingle(student));

        verify(mockJdbcStudent, times(1)).isSingle(any(Student.class));
    }

    @Test
    void deleteById_deletedStudent_deleted() {
        studentService.deleteById(1L);

        verify(mockJdbcStudent, times(1)).deleteById(1L);
    }

    @Test
    void findById_findStudent_found() {
        Student student = new Student();
        student.setId(1L);

        when(mockJdbcStudent.findById(1L)).thenReturn(student);
        Student result = studentService.findById(1L);

        assertEquals(student, result);
    }

    @Test
    void findAll_findAllStudents_foundAll() {
        List<Student> studentList = List.of(new Student(), new Student());

        when(mockJdbcStudent.findAll()).thenReturn(studentList);
        List<Student> result = studentService.findAll();

        assertEquals(studentList, result);
    }
}
