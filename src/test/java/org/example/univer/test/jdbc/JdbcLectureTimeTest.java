package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcLectureTime;
import org.example.univer.models.LectureTime;
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
    private JdbcLectureTime jdbcLectureTime;
    private final static String TABLE_NAME = "lectiontime";
    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void checkCreatedLectureTime() {
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
    void checkUpdateLectureTime() {
        LectureTime lectureTime = jdbcLectureTime.findById(1L);
        lectureTime.setStart(LocalDateTime.parse("2024-04-05 10:00:00", formatter1));
        jdbcLectureTime.update(lectureTime);

        assertEquals("2024-04-05 10:00:00", jdbcLectureTime.findById(1L).getStart());
    }

    @Test
    void checkFindByIdLectureTime() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(19L);
        lectureTime.setStart(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));
        jdbcLectureTime.create(lectureTime);

        assertEquals(jdbcLectureTime.findById(19L), lectureTime);
    }

    @Test
    void checkDeletedLectureTime() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcLectureTime.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllLectureTime() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcLectureTime.findAll().size();

        assertEquals(expected, actual);
    }
}
