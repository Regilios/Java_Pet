package org.example.univer.repositories;

import org.example.univer.models.Gender;
import org.example.univer.models.Teacher;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class TeacherRepositoryTest {
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void whenSaveTeacher_thenTeacherIsPersisted() {
        Teacher teacher = createTeacher();
        Teacher saved = teacherRepository.save(teacher);

        assertNotNull(saved.getId());
        assertEquals("John", saved.getFirstName());
    }

    @Test
    void whenDeleteTeacher_thenTeacherIsRemoved() {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        teacherRepository.deleteById(teacher.getId());

        Optional<Teacher> found = teacherRepository.findById(teacher.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void whenExistsByFirstNameAndLastName_thenReturnTrue() {
        Teacher teacher = createTeacher();
        teacherRepository.save(teacher);

        boolean exists = teacherRepository.existsByFirstNameAndLastName("John", "Doe");

        assertTrue(exists);
    }

    @Test
    void whenFindAll_thenReturnAllTeachers() {
        Teacher teacher = createTeacher();
        Teacher teacher2 = createTeacher();
        teacher2.setFirstName("Test");

        teacherRepository.saveAll(List.of(teacher, teacher2));

        List<Teacher> teachers = teacherRepository.findAll();

        assertTrue(teachers.size() >= 2);
        assertTrue(teachers.stream().anyMatch(g -> g.getFirstName().equals("John")));
        assertTrue(teachers.stream().anyMatch(g -> g.getFirstName().equals("Test")));
    }

    private Teacher createTeacher() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setBirthday(LocalDate.of(1980, 1, 1));
        teacher.setEmail("john@example.com");
        teacher.setPhone("1234567890");
        teacher.setAddress("Street");
        teacher.setGender(Gender.MALE);

        return teacher;
    }
}
