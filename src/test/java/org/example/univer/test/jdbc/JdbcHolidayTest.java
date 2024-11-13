package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcHoliday;
import org.example.univer.models.Holiday;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;

import static junit.framework.Assert.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class JdbcHolidayTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcHoliday jdbcHoliday;
    private final static String TABLE_NAME = "holiday";

    @Test
    void checkCreatedTeacher() {
        Holiday holiday = new Holiday();
        holiday.setId(3L);
        holiday.setDesc("test");
        holiday.setStartHoliday(LocalDate.parse("2024-01-01"));
        holiday.setEndHoliday(LocalDate.parse("2024-01-10"));
        jdbcHoliday.create(holiday);

        Holiday holiday1 = jdbcHoliday.findById(3L);

        assertEquals(holiday, holiday1);
        assertEquals(holiday.getId(), holiday1.getId());
        assertEquals(holiday.getStartHolidayLocal(), holiday1.getStartHolidayLocal());
        assertEquals(holiday.getEndHolidayLocal(), holiday1.getEndHolidayLocal());
    }

    @Test
    void checkUpdateTeacher() {
        Holiday holiday = jdbcHoliday.findById(1L);
        holiday.setDesc("1111");
        jdbcHoliday.update(holiday);

        assertEquals("1111", jdbcHoliday.findById(1L).getDesc());
    }

    @Test
    void checkFindByIdTeacher() {
        Holiday holiday = new Holiday();
        holiday.setId(3L);
        holiday.setDesc("test");
        holiday.setStartHoliday(LocalDate.parse("2024-01-01"));
        holiday.setEndHoliday(LocalDate.parse("2024-01-10"));
        jdbcHoliday.create(holiday);

        assertEquals(jdbcHoliday.findById(3L), holiday);
    }

    @Test
    void checkDeletedTeacher() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcHoliday.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllTeacher() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcHoliday.findAll().size();

        assertEquals(expected, actual);
    }
}
