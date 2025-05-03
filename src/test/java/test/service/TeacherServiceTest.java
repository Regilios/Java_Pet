package test.service;

import org.example.univer.dao.interfaces.DaoTeacherInterface;
import org.example.univer.exeption.TeacherExeption;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Mock
    private DaoTeacherInterface mockTeacher;
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

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.FEMALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(cathedra);

        when(mockTeacher.isSingle(teacher)).thenReturn(false);
        teacherService.create(teacher);

        verify(mockTeacher, times(1)).create(teacher);
    }

    @Test
    void create_teacherGenderNotCorrect_throwException() {
        Cathedra cathedra = new Cathedra();

        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.MALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(cathedra);

        when(mockTeacher.isSingle(teacher)).thenReturn(false);

        assertThrows(TeacherExeption.class, () -> {
            teacherService.validate(teacher, TeacherService.ValidationContext.METHOD_CREATE);
            teacherService.create(teacher);
        });
        verify(mockTeacher, never()).create(any(Teacher.class));
    }

    @Test
    void isSingle_teacherIsSingle_true() {
        Teacher teacher = new Teacher();

        when(mockTeacher.isSingle(teacher)).thenReturn(true);
        assertTrue(teacherService.isSingle(teacher));

        verify(mockTeacher, times(1)).isSingle(any(Teacher.class));
    }

    @Test
    void deleteById_deletedTeacher_deleted() {
        Teacher teacher = new Teacher();
        teacherService.deleteEntity(teacher);

        verify(mockTeacher, times(1)).deleteEntity(teacher);
    }

    @Test
    void findById_findStudent_found() {
        Teacher teacher = new Teacher();

        when(mockTeacher.findById(1L)).thenReturn(Optional.of(teacher));
        Optional<Teacher> result = teacherService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(teacher, result.get());
    }

    @Test
    void findAll_findAllStudents_foundAll() {
        List<Teacher> teacherList = List.of(new Teacher(), new Teacher());

        when(mockTeacher.findAll()).thenReturn(teacherList);
        List<Teacher> result = teacherService.findAll();

        assertEquals(teacherList, result);
    }
}
