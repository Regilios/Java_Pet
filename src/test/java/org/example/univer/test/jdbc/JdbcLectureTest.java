package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.*;
import org.example.univer.models.Lecture;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class JdbcLectureTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcLecture jdbcLecture;
    @Autowired
    private JdbcCathedra jdbcCathedra;
    @Autowired
    private JdbcTeacher jdbcTeacher;
    @Autowired
    private JdbcSubject jdbcSubject;
    @Autowired
    private JdbcAudience jdbcAudience;
    @Autowired
    private JdbcLectureTime jdbcLectureTime;

    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
    private final static String TABLE_NAME = "lection";

    @Test
    void checkCreatedLecture() {
        Lecture lecture = new Lecture();
        lecture.setId(17L);
        lecture.setCathedra(jdbcCathedra.findById(1L));
        lecture.setTeacher(jdbcTeacher.findById(1L));
        lecture.setSubject(jdbcSubject.findById(1L));
        lecture.setTime(jdbcLectureTime.findById(1L));
        lecture.setAudience(jdbcAudience.findById(1L));
        jdbcLecture.create(lecture);

        Lecture lecture1 = jdbcLecture.findById(17L);

        assertEquals(lecture, lecture1);
    }

    @Test
    void checkUpdateLecture() {
        Lecture lecture = jdbcLecture.findById(1L);
        lecture.setTeacher(jdbcTeacher.findById(2L));
        jdbcLecture.update(lecture);

        assertEquals(jdbcTeacher.findById(2L), jdbcLecture.findById(1L).getTeacher());
    }

    @Test
    void checkFindByIdLecture() {
        Lecture lecture = new Lecture();
        lecture.setId(17L);
        lecture.setCathedra(jdbcCathedra.findById(1L));
        lecture.setTeacher(jdbcTeacher.findById(1L));
        lecture.setSubject(jdbcSubject.findById(1L));
        lecture.setTime(jdbcLectureTime.findById(1L));
        lecture.setAudience(jdbcAudience.findById(1L));
        jdbcLecture.create(lecture);

        assertEquals(jdbcLecture.findById(17L), lecture);
    }

    @Test
    void checkDeletedLecture() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcLecture.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllLecture() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcLecture.findAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void checkGetTimetableTeacher() {
        assertEquals(jdbcLecture.findById(1L), jdbcLecture.getTimetableTeacher(jdbcTeacher.findById(1L), LocalDate.parse("2024-02-02")).get(0));
    }

    @Test
    void checkIsSingleLecture() {
        Lecture lecture = new Lecture();
        lecture.setId(17L);
        lecture.setCathedra(jdbcCathedra.findById(1L));
        lecture.setTeacher(jdbcTeacher.findById(2L));
        lecture.setSubject(jdbcSubject.findById(3L));
        lecture.setTime(jdbcLectureTime.findById(4L));
        lecture.setAudience(jdbcAudience.findById(4L));

        assertFalse(jdbcLecture.isSingle(lecture));
    }

    @Test
    void checkFindByAudienceDateAndLectureTime() {
        assertFalse(jdbcLecture.findByAudienceDateAndLectureTime(jdbcAudience.findById(4L),jdbcLectureTime.findById(4L)));
    }
}
