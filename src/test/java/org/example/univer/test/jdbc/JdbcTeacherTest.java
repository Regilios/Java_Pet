package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcCathedra;
import org.example.univer.dao.jdbc.JdbcTeacher;
import org.example.univer.models.Gender;
import org.example.univer.models.Teacher;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("jdbc")
public class JdbcTeacherTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcTeacher jdbcTeacher;
    @Autowired
    private JdbcCathedra jdbcCathedra;
    private final static String TABLE_NAME = "teacher";

    @Test
    void checkCreatedTeacher() {
        Teacher teacher = new Teacher();

        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.MALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(jdbcCathedra.findById(1L));
        jdbcTeacher.create(teacher);

        Teacher teacher1 = jdbcTeacher.findById(teacher.getId());

        assertEquals(teacher, teacher1);
        assertEquals(teacher.getId(), teacher1.getId());
        assertEquals(teacher.getFirstName(), teacher1.getFirstName());
        assertEquals(teacher.getLastName(), teacher1.getLastName());
    }

    @Test
    void checkUpdateTeacher() {
        Teacher teacher = jdbcTeacher.findById(1L);
        teacher.setPhone("1111");
        jdbcTeacher.update(teacher);

        assertEquals("1111", jdbcTeacher.findById(1L).getPhone());
    }

    @Test
    void checkFindByIdTeacher() {
        Teacher teacher = new Teacher();

        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.MALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(jdbcCathedra.findById(1L));
        jdbcTeacher.create(teacher);

        assertEquals(jdbcTeacher.findById(teacher.getId()), teacher);
    }

    @Test
    void checkDeletedTeacher() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcTeacher.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllTeacher() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcTeacher.findAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void checkIsSingleTeacher() {
        Teacher teacher = new Teacher();

        teacher.setFirstName("test");
        teacher.setLastName("test2");
        teacher.setGender(Gender.MALE);
        teacher.setAddress("test");
        teacher.setEmail("test@test");
        teacher.setPhone("test");
        teacher.setBirthday(LocalDate.parse("1983-02-01"));
        teacher.setCathedra(jdbcCathedra.findById(1L));

        assertFalse(jdbcTeacher.isSingle(teacher));
    }
}
