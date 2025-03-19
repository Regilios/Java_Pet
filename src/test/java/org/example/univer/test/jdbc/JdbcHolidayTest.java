package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcHoliday;
import org.example.univer.models.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("jdbc")
public class JdbcHolidayTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcHoliday jdbcHoliday;
    private final static String TABLE_NAME = "holiday";

    @Test
    void checkCreatedHoliday() {
        Holiday holiday = new Holiday();
        holiday.setDesc("test");
        holiday.setStart_holiday(LocalDate.parse("2024-01-01"));
        holiday.setEnd_holiday(LocalDate.parse("2024-01-30"));
        jdbcHoliday.create(holiday);

        Holiday holiday1 = jdbcHoliday.findById(holiday.getId());

        assertEquals(holiday, holiday1);
        assertEquals(holiday.getId(), holiday1.getId());
        assertEquals(holiday.getStartHolidayLocal(), holiday1.getStartHolidayLocal());
        assertEquals(holiday.getEndHolidayLocal(), holiday1.getEndHolidayLocal());
    }

    @Test
    void checkUpdateHoliday() {
        Holiday holiday = jdbcHoliday.findById(1L);
        holiday.setDesc("1111");
        holiday.setStart_holiday(LocalDate.parse("2024-01-01"));
        holiday.setEnd_holiday(LocalDate.parse("2024-01-30"));
        jdbcHoliday.update(holiday);

        assertEquals("1111", jdbcHoliday.findById(1L).getDesc());
    }

    @Test
    void checkFindByIdHoliday() {
        Holiday holiday = new Holiday();

        holiday.setDesc("test");
        holiday.setStart_holiday(LocalDate.parse("2024-01-01"));
        holiday.setEnd_holiday(LocalDate.parse("2024-01-10"));
        jdbcHoliday.create(holiday);

        assertEquals(jdbcHoliday.findById(holiday.getId()), holiday);
    }

    @Test
    void checkDeletedHoliday() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcHoliday.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllHoliday() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcHoliday.findAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void checkIsSingleHoliday() {
        Holiday holiday = new Holiday();

        holiday.setDesc("test");
        holiday.setStart_holiday(LocalDate.parse("2024-01-01"));
        holiday.setEnd_holiday(LocalDate.parse("2024-01-30"));

        assertFalse(jdbcHoliday.isSingle(holiday));
    }

    @Test
    void checkLectureDoesNotFallOnHoliday() {
        assertTrue(jdbcHoliday.lectureDoesNotFallOnHoliday(LocalDateTime.parse("2024-01-07T00:00:00")));
    }
}
