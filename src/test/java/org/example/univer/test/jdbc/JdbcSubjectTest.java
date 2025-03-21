package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcSubject;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("jdbc")
public class JdbcSubjectTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcSubject jdbcSubject;

    private final static String TABLE_NAME = "subject";

    @Test
    void checkCreatedSubject() {
        Subject subject = new Subject();

        subject.setName("test");
        subject.setDescription("test");
        jdbcSubject.create(subject);
        Subject subject1 = jdbcSubject.findById(subject.getId());

        assertEquals(subject, subject1);
        assertEquals(subject.getId(), subject1.getId());
        assertEquals(subject.getName(), subject1.getName());
        assertEquals(subject.getDescription(), subject1.getDescription());
    }

    @Test
    void checkUpdateSubject() {
        Subject subject = jdbcSubject.findById(1L);
        subject.setName("111");
        jdbcSubject.update(subject);

        assertEquals("111", jdbcSubject.findById(1L).getName());
    }

    @Test
    void checkFindByIdSubject() {
        Subject subject = new Subject();

        subject.setName("test");
        subject.setDescription("test");
        jdbcSubject.create(subject);

        assertEquals(jdbcSubject.findById(subject.getId()), subject);
    }

    @Test
    void checkDeletedSubject() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcSubject.deleteById(2L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllSubject() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcSubject.findAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void checkIsSingleSubject() {
        Subject subject = new Subject();

        subject.setName("test");
        subject.setDescription("test");

        assertFalse(jdbcSubject.isSingle(subject));
    }

    @Test
    void checkTeacherAssignedSubject() {
        Subject subject = new Subject();
        subject.setId(1000L);
        Teacher teacher = new Teacher();
        teacher.setId(5L);

        assertFalse(jdbcSubject.checkTeacherAssignedSubject(teacher, subject));
    }
}
