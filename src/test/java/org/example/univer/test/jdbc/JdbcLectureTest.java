package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.*;
import org.example.univer.models.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

import static junit.framework.Assert.assertEquals;
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
    void checkCreatedLection() {
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
    void checkUpdateLection() {
        Lecture lecture = jdbcLecture.findById(1L);
        lecture.setTeacher(jdbcTeacher.findById(2L));
        jdbcLecture.update(lecture);

        assertEquals(jdbcTeacher.findById(2L), jdbcLecture.findById(1L).getTeacher());
    }

    @Test
    void checkFindByIdLection() {
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
    void checkDeletedLection() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcLecture.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllLection() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcLecture.findAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void checkGetTimetableStudent() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        lectureTime.setStart(LocalDateTime.parse("2024-02-02 14:00:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2024-02-02 16:00:00", formatter1));
        jdbcLectureTime.create(lectureTime);

        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);
        cathedra.setName("test");
        jdbcCathedra.create(cathedra);

        Teacher teacher = new Teacher();
        teacher.setId(1L);
        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.MALE);
        teacher.setAddres("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(cathedra);
        jdbcTeacher.create(teacher);

        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("test");
        subject.setDescription("test");
        jdbcSubject.create(subject);

        Audience audience = new Audience();
        audience.setId(1L);
        audience.setRoom(1);
        audience.setCapacity(100);
        jdbcAudience.create(audience);

        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lecture.setCathedra(cathedra);
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(lectureTime);
        lecture.setAudience(audience);
        jdbcLecture.create(lecture);


        assertEquals(jdbcLecture.findById(1L), jdbcLecture.getTimetable(jdbcTeacher.findById(1L), LocalDate.parse("2024-02-02")).get(0));
    }
}
