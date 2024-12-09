package org.example.univer.test.jdbc;


import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcTeacher;
import org.example.univer.dao.jdbc.JdbcVacation;
import org.example.univer.models.Vacation;
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
public class JdbcVacationTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcVacation jdbcVacation;
    @Autowired
    private JdbcTeacher jdbcTeacher;
    private final static String TABLE_NAME = "vacation";

    @Test
    void checkCreatedVacation() {
        Vacation vacation = new Vacation();
        vacation.setId(5L);
        vacation.setStartJob(LocalDate.parse("2024-07-01"));
        vacation.setEndJob(LocalDate.parse("2024-07-14"));
        vacation.setTeacher(jdbcTeacher.findById(1L));
        jdbcVacation.create(vacation);

        Vacation vacation1 = jdbcVacation.findById(5L);

        assertEquals(vacation, vacation1);
        assertEquals(vacation.getId(), vacation1.getId());
        assertEquals(vacation.getStartJobLocal(), vacation1.getStartJobLocal());
        assertEquals(vacation.getEndJobLocal(), vacation1.getEndJobLocal());
    }

    @Test
    void checkUpdateVacation() {
        Vacation vacation = jdbcVacation.findById(1L);
        vacation.setTeacher(jdbcTeacher.findById(1L));
        jdbcVacation.update(vacation);

        assertEquals(jdbcTeacher.findById(1L), jdbcVacation.findById(1L).getTeacher());
    }

    @Test
    void checkFindByIdVacation() {
        Vacation vacation = new Vacation();
        vacation.setId(5L);
        vacation.setStartJob(LocalDate.parse("2024-02-01"));
        vacation.setEndJob(LocalDate.parse("2035-02-01"));
        vacation.setTeacher(jdbcTeacher.findById(1L));
        jdbcVacation.create(vacation);

        assertEquals(jdbcVacation.findById(5L), vacation);
    }

    @Test
    void checkDeletedVacation() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcVacation.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllVacation() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcVacation.findAll().size();

        assertEquals(expected, actual);
    }
}
