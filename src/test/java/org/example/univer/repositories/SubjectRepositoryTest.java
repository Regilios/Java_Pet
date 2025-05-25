package org.example.univer.repositories;

import org.example.univer.models.Gender;
import org.example.univer.models.Subject;
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
public class SubjectRepositoryTest {
    @Autowired
    private SubjectRepository subjectRepository;

    @Test
    void whenSaveSubject_thenSubjectIsPersisted() {
        Subject subject = new Subject();
        subject.setName("Test subject");
        subject.setDescription("Test subject Test subject Test subject");
        Subject saved = subjectRepository.save(subject);

        assertNotNull(saved.getId());
        assertEquals("Test subject", saved.getName());
    }

    @Test
    void whenDeleteSubject_thenSubjectIsRemoved() {
        Subject subject = new Subject();
        subject.setName("Test subject");
        subject.setDescription("Test subject Test subject Test subject");
        subjectRepository.save(subject);

        subjectRepository.deleteById(subject.getId());

        Optional<Subject> found = subjectRepository.findById(subject.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void whenExistsByName_thenReturnTrue() {
        Subject subject = new Subject();
        subject.setName("Test subject");
        subject.setDescription("Test subject Test subject Test subject");
        subjectRepository.save(subject);

        boolean exists = subjectRepository.existsByName("Test subject");

        assertTrue(exists);
    }
    @Test
    void whenExistsByTeachers_IdAndId_thenReturnTrue() {
        Subject subject = new Subject();
        subject.setName("Test subject");
        subject.setDescription("Test subject Test subject Test subject");

        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setBirthday(LocalDate.of(1980, 1, 1));
        teacher.setEmail("john@example.com");
        teacher.setPhone("1234567890");
        teacher.setAddress("Street");
        teacher.setGender(Gender.MALE);
        teacher.setSubjects(List.of(subject));

        boolean exists = subjectRepository.existsByTeachers_IdAndId(teacher.getId(), subject.getId());
        assertFalse(exists);
    }


    @Test
    void whenFindAll_thenReturnAllSubjects() {
        Subject subject = new Subject();
        subject.setName("Test subject");
        subject.setDescription("Test subject Test subject Test subject");
        Subject subject2= new Subject();
        subject2.setName("Test subject 2");
        subject2.setDescription("Test subject Test subject Test subject 2");

        subjectRepository.saveAll(List.of(subject, subject2));

        List<Subject> subjects = subjectRepository.findAll();

        assertTrue(subjects.size() >= 2);
        assertTrue(subjects.stream().anyMatch(g -> g.getName().equals("Test subject")));
        assertTrue(subjects.stream().anyMatch(g -> g.getName().equals("Test subject 2")));
    }
}
