package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoStudentInterfaces;
import org.example.univer.dao.mapper.StudentMapper;
import org.example.univer.dao.models.Group;
import org.example.univer.dao.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcStudent implements DaoStudentInterfaces {

    private final JdbcTemplate jdbcTemplate;
    private StudentMapper studentMapper;

    private static final String SQL_UPDATE = "UPDATE students SET firstName=?, lastName=?, gender=?, address=?, email=?, phone=?, birthday=?, group_id=? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM students WHERE id = ?";
    private static final String SQL_GET_BY_ID = "SELECT * FROM students WHERE id = ?";
    private static final String SQL_FIND_STUDENT_BY_GROUP_ID = "SELECT * FROM students WHERE group_id = ? ORDER BY id";
    private static final String SQL_FIND_ALL = "SELECT * FROM students ORDER BY id";
    private static final String SQL_CREATE = "INSERT INTO students (firstName, lastName, gender, address, email, phone, birthday, group_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";

    @Autowired
    public JdbcStudent(JdbcTemplate jdbcTemplate, StudentMapper studentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentMapper = studentMapper;
    }

    @Override
    public void create(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getGender());
            ps.setString(4, student.getAddres());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getPhone());
            ps.setObject(7, student.getBirthday());
            ps.setLong(8, student.getGroup().getId());

            return ps;
        }, keyHolder);
       // System.out.println(keyHolder.getKey());
    }

    @Override
    public void update(Student student) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getGender());
            ps.setString(4, student.getAddres());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getPhone());
            ps.setObject(7, student.getBirthday());
            ps.setLong(8, student.getGroup().getId());
            ps.setLong(9, student.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Student findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, studentMapper, id);
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, studentMapper);
    }

    public List<Student> findAllStudentByIdGroup(Group group) {
        Long id = group.getId();
        return jdbcTemplate.query(SQL_FIND_STUDENT_BY_GROUP_ID, studentMapper, id);
    }
}
