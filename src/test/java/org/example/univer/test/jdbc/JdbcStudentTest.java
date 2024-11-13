package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcGroup;
import org.example.univer.dao.jdbc.JdbcStudent;
import org.example.univer.models.Gender;
import org.example.univer.models.Student;
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
public class JdbcStudentTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcGroup jdbcGroup;
    @Autowired
    private JdbcStudent jdbcStudent;
    private final static String TABLE_NAME = "students";

    @Test
    void checkCreatedGroup() {
        Student student = new Student();
        student.setId(7L);
        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddres("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991,10,17));
        student.setGroup(jdbcGroup.findById(2L));
        jdbcStudent.create(student);
        Student student1 = jdbcStudent.findById(7L);

        assertEquals(student, student1);
        assertEquals(student.getId(), student1.getId());
        assertEquals(student.getFirstName(), student1.getFirstName());
        assertEquals(student.getPhone(), student1.getPhone());
    }

    @Test
    void checkUpdateStudent() {
        Student student = jdbcStudent.findById(1L);
        student.setFirstName("test");
        jdbcStudent.update(student);

        assertEquals("test", jdbcStudent.findById(1L).getFirstName());
    }

    @Test
    void checkFindByIdGroup() {
        Student student = new Student();
        student.setId(7L);
        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddres("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991,10,17));
        student.setGroup(jdbcGroup.findById(2L));
        jdbcStudent.create(student);

        assertEquals(jdbcStudent.findById(7L), student);
    }

    @Test
    void checkDeletedStudent() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcStudent.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllStudents() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcStudent.findAll().size();

        assertEquals(expected, actual);
    }
}
