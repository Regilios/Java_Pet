package test.service;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoStudentInterface;
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
public class StudentServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private DaoStudentInterface mockStudent;
    @InjectMocks
    private StudentService studentService;

    @BeforeEach
    void setUp() {
        appSettings.setMaxGroupSize(4);
        studentService = new StudentService(mockStudent, appSettings);
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

        when(mockStudent.isSingle(student)).thenReturn(false);
        when(mockStudent.checkGroupSize(student)).thenReturn(3);
        studentService.create(student);

        verify(mockStudent, times(1)).create(student);
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

        when(mockStudent.isSingle(student)).thenReturn(false);
        when(mockStudent.checkGroupSize(student)).thenReturn(6);

        assertThrows(StudentExeption.class, () -> {
            studentService.validate(student, StudentService.ValidationContext.METHOD_CREATE);
            studentService.create(student);
        });
        verify(mockStudent, never()).create(any(Student.class));
    }

    @Test
    void isSingle_studentIsSingle_true() {
        Student student = new Student();

        when(mockStudent.isSingle(student)).thenReturn(true);
        assertTrue(studentService.isSingle(student));

        verify(mockStudent, times(1)).isSingle(any(Student.class));
    }

    @Test
    void deleteById_deletedStudent_deleted() {
        Student student = new Student();
        student.setId(1L);
        studentService.deleteById(1L);

        verify(mockStudent, times(1)).deleteById(1L);
    }

    @Test
    void findById_findStudent_found() {
        Student student = new Student();
        student.setId(1L);

        when(mockStudent.findById(1L)).thenReturn(Optional.of(student));
        Optional<Student> result = studentService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(student, result.get());
    }

    @Test
    void findAll_findAllStudents_foundAll() {
        List<Student> studentList = List.of(new Student(), new Student());

        when(mockStudent.findAll()).thenReturn(studentList);
        List<Student> result = studentService.findAll();

        assertEquals(studentList, result);
    }
}
