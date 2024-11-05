package org.example.univer.test;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbsLectureTime;
import org.example.univer.dao.models.LectureTime;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static junit.framework.Assert.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class JdbcLectureTimeTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbsLectureTime jdbcLectureTime;
    private final static String TABLE_NAME = "lectiontime";
    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void checkCreatedTeacher() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(19L);
        lectureTime.setStart(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));
        jdbcLectureTime.create(lectureTime);

        LectureTime lectureTime1 = jdbcLectureTime.findById(19L);

        assertEquals(lectureTime, lectureTime1);
        assertEquals(lectureTime.getId(), lectureTime1.getId());
        assertEquals(lectureTime.getStartLocal(), lectureTime1.getStartLocal());
        assertEquals(lectureTime.getEndLocal(), lectureTime1.getEndLocal());
    }

    @Test
    void checkUpdateTeacher() {
        LectureTime lectureTime = jdbcLectureTime.findById(1L);
        lectureTime.setStart(LocalDateTime.parse("2024-04-05 10:00:00", formatter1));

        assertEquals("2024-04-05 10:00:00", lectureTime.getStart());
    }

    @Test
    void checkFindByIdTeacher() {
        LectureTime lectureTime = jdbcLectureTime.findById(1L);

        assertEquals(jdbcLectureTime.findById(1L), lectureTime);
    }

    @Test
    void checkDeletedTeacher() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcLectureTime.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllTeacher() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcLectureTime.findAll().size();

        assertEquals(expected, actual);
    }
}