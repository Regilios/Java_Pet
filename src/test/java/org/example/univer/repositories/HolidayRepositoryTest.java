package org.example.univer.repositories;

import org.example.univer.models.Holiday;
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
public class HolidayRepositoryTest {
    @Autowired
    private HolidayRepository holidayRepository;

    @Test
    void whenSaveHoliday_thenGroupIsPersisted() {
        Holiday holiday = new Holiday();
        holiday.setDescription("test test test test test test");
        holiday.setStartHoliday(LocalDate.parse("2024-01-02"));
        holiday.setEndHoliday(LocalDate.parse("2024-01-14"));
        Holiday saved = holidayRepository.save(holiday);

        assertNotNull(saved.getId());
        assertEquals("test test test test test test", saved.getDescription());
    }

    @Test
    void whenDeleteHoliday_thenHolidaysIsRemoved() {
        Holiday holiday = new Holiday();
        holiday.setDescription("test test test test test test");
        holiday.setStartHoliday(LocalDate.parse("2024-01-02"));
        holiday.setEndHoliday(LocalDate.parse("2024-01-14"));
        holidayRepository.save(holiday);

        holidayRepository.deleteById(holiday.getId());

        Optional<Holiday> found = holidayRepository.findById(holiday.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void whenExistsByDescription_thenReturnTrue() {
        Holiday holiday = new Holiday();
        holiday.setDescription("test test test test test test");
        holiday.setStartHoliday(LocalDate.parse("2024-01-02"));
        holiday.setEndHoliday(LocalDate.parse("2024-01-14"));
        holidayRepository.save(holiday);

        boolean exists = holidayRepository.existsByDescription("test test test test test test");

        assertTrue(exists);
    }

    @Test
    void whenFindAll_thenReturnAllHolidays() {
        Holiday h1 = new Holiday();
        h1.setDescription("test test test test test test 1");
        h1.setStartHoliday(LocalDate.parse("2024-01-02"));
        h1.setEndHoliday(LocalDate.parse("2024-01-14"));
        Holiday h2 = new Holiday();
        h2.setDescription("test test test test test test 2");
        h2.setStartHoliday(LocalDate.parse("2024-01-02"));
        h2.setEndHoliday(LocalDate.parse("2024-01-14"));

        holidayRepository.saveAll(List.of(h1, h2));

        List<Holiday> groups = holidayRepository.findAll();

        assertTrue(groups.size() >= 2);
        assertTrue(groups.stream().anyMatch(g -> g.getDescription().equals("test test test test test test 1")));
        assertTrue(groups.stream().anyMatch(g -> g.getDescription().equals("test test test test test test 2")));
    }
}
