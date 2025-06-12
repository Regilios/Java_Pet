package org.example.univer.service;

import org.example.univer.config.AppSettings;
import org.example.univer.exeption.TeacherExeption;
import org.example.univer.models.Cathedra;
import org.example.univer.models.Gender;
import org.example.univer.models.Teacher;
import org.example.univer.repositories.TeacherRepository;
import org.example.univer.services.SubjectService;
import org.example.univer.services.TeacherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class TeacherServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private TeacherRepository mockTeacher;
    @Mock
    private SubjectService subjectService;
    @InjectMocks
    private TeacherService teacherService;

    @BeforeEach
    void setUp() {
        appSettings.setGenderTeacher("FEMALE");
        teacherService.init();
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

        when(mockTeacher.existsByFirstNameAndLastName(teacher.getFirstName(),teacher.getLastName())).thenReturn(false);
        teacherService.create(teacher);

        verify(mockTeacher, times(1)).save(teacher);
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

        when(mockTeacher.existsByFirstNameAndLastName(teacher.getFirstName(),teacher.getLastName())).thenReturn(false);

        assertThrows(TeacherExeption.class, () -> {
            teacherService.validate(teacher, TeacherService.ValidationContext.METHOD_CREATE);
            teacherService.create(teacher);
        });
        verify(mockTeacher, never()).save(any(Teacher.class));
    }

    @Test
    void isSingle_teacherIsSingle_true() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        when(mockTeacher.existsByFirstNameAndLastName(teacher.getFirstName(),teacher.getLastName())).thenReturn(true);
        assertTrue(teacherService.isSingle(teacher));

        verify(mockTeacher, times(1)).existsByFirstNameAndLastName(anyString(),anyString());
    }

    @Test
    void deleteById_deletedTeacher_deleted() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacherService.deleteById(1L);

        verify(mockTeacher, times(1)).deleteById(1L);
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
