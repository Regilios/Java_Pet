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
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("jdbc")
public class JdbcStudentTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcGroup jdbcGroup;
    @Autowired
    private JdbcStudent jdbcStudent;
    private final static String TABLE_NAME = "students";


    @Test
    void checkCreatedStudent() {
        Student student = new Student();

        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddress("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991,10,17));
        student.setGroup(jdbcGroup.findById(2L));
        jdbcStudent.create(student);
        Student student1 = jdbcStudent.findById(student.getId());

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
    void checkFindByIdStudent() {
        Student student = new Student();

        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddress("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991,10,17));
        student.setGroup(jdbcGroup.findById(2L));
        jdbcStudent.create(student);

        assertEquals(jdbcStudent.findById(student.getId()).getId(), student.getId());
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

    @Test
    void checkIsSingleStudent() {
        Student student = new Student();

        student.setFirstName("Pavel");
        student.setLastName("Yarinov");
        student.setGender(Gender.MALE);
        student.setAddress("Armany str 24");
        student.setEmail("pavel@gmail.com");
        student.setPhone("8978474666");
        student.setBirthday(LocalDate.of(1991,10,17));
        student.setGroup(jdbcGroup.findById(2L));

        assertFalse(jdbcStudent.isSingle(student));
    }

    @Test
    void checkGroupSize() {
        Student student = new Student();
        student.setGroup(jdbcGroup.findById(1L));
        List<Student> groupList = jdbcStudent.findAll().stream().filter(x-> x.getGroup().getId().equals(1L)).collect(Collectors.toList());

        assertEquals(Optional.of(groupList.size()), Optional.of(jdbcStudent.checkGroupSize(student)));
    }

    @Test
    void checkFindAllStudentByGroupId() {
        List<Student> groupList = jdbcStudent.findAll().stream().filter(x-> x.getGroup().getId().equals(1L)).collect(Collectors.toList());

        assertEquals(groupList, jdbcStudent.findAllStudentByGroupId(jdbcGroup.findById(1L)));
    }
}
