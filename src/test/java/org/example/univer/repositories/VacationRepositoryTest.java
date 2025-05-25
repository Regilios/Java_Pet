package org.example.univer.repositories;

import org.example.univer.models.Gender;
import org.example.univer.models.Teacher;
import org.example.univer.models.Vacation;
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
public class VacationRepositoryTest {
    @Autowired
    private VacationRepository vacationRepository;
    @Autowired
    private TeacherRepository teacherRepository;

    @Test
    void whenSaveVacation_thenVacationIsPersisted() {
        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2025-01-01"));
        vacation.setEndJob(LocalDate.parse("2025-01-11"));
        Vacation saved = vacationRepository.save(vacation);

        assertNotNull(saved.getId());
        assertEquals(LocalDate.parse("2025-01-01"), saved.getStartJob());
    }

    @Test
    void whenDeleteVacation_thenVacationIsRemoved() {
        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2025-01-01"));
        vacation.setEndJob(LocalDate.parse("2025-01-11"));
        vacationRepository.save(vacation);

        vacationRepository.deleteById(vacation.getId());

        Optional<Vacation> found = vacationRepository.findById(vacation.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void whenExistsByStartJobAndEndJobAndTeacher_Id_thenReturnTrue() {
        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setBirthday(LocalDate.of(1980, 1, 1));
        teacher.setEmail("john@example.com");
        teacher.setPhone("1234567890");
        teacher.setAddress("Street");
        teacher.setGender(Gender.MALE);
        teacher = teacherRepository.save(teacher);

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2025-01-01"));
        vacation.setEndJob(LocalDate.parse("2025-01-11"));
        vacation.setTeacher(teacher);

        vacationRepository.save(vacation);

        boolean exists = vacationRepository.existsByStartJobAndEndJobAndTeacher_Id(
              LocalDate.parse("2025-01-01"),
              LocalDate.parse("2025-01-11"),
              teacher.getId());

        assertTrue(exists);
    }

    @Test
    void whenFindAll_thenReturnAllGroupsWithCathedra() {
        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2025-01-01"));
        vacation.setEndJob(LocalDate.parse("2025-01-11"));
        Vacation vacation2 = new Vacation();
        vacation2.setStartJob(LocalDate.parse("2025-02-01"));
        vacation2.setEndJob(LocalDate.parse("2025-02-11"));

        vacationRepository.saveAll(List.of(vacation, vacation2));

        List<Vacation> vacations = vacationRepository.findAll();

        assertTrue(vacations.size() >= 2);
        assertTrue(vacations.stream().anyMatch(g -> g.getStartJob().equals(LocalDate.parse("2025-01-01"))));
        assertTrue(vacations.stream().anyMatch(g -> g.getStartJob().equals(LocalDate.parse("2025-02-01"))));
    }
}
