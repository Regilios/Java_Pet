package org.example.univer.repositories;

import org.example.univer.models.Gender;
import org.example.univer.models.Student;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class StudentRepositoryTest {
    @Autowired
    private StudentRepository studentRepository;

    @Test
    void whenSaveStudent_thenStudentIsPersisted() {
        Student student = createStudent();
        Student saved = studentRepository.save(student);

        assertNotNull(saved.getId());
        assertEquals("Pavel", saved.getFirstName());
    }

    @Test
    void whenFindById_thenStudentIsReturned() {
        Student student = createStudent();
        studentRepository.save(student);

        Optional<Student> found = studentRepository.findById(student.getId());

        assertTrue(found.isPresent());
        assertEquals("Pavel", found.get().getFirstName());
    }

    @Test
    void whenDeleteStudent_thenStudentIsRemoved() {
        Student student = createStudent();

        studentRepository.save(student);
        studentRepository.deleteById(student.getId());

        Optional<Student> found = studentRepository.findById(student.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void whenExistsByFirstNameAndLastName_thenReturnTrue() {
        Student student = createStudent();
        studentRepository.save(student);

        boolean exists = studentRepository.existsByFirstNameAndLastName("Pavel", "Yarinov");
        assertTrue(exists);
    }

    @Test
    void whenFindAllPaginated_thenPageIsReturned() {
        Student student = createStudent();
        Student student2 = createStudent();
        student2.setFirstName("111");
        studentRepository.saveAll(List.of(student, student2));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Student> page = studentRepository.findAllByOrderById(pageable);

        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getContent().size());
    }

    private Student createStudent() {
        Student student = new Student();
        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddress("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.parse("1991-01-01"));
        return student;
    }
}
