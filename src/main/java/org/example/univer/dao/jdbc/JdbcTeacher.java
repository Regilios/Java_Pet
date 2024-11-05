package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoTeacherInterfaces;
import org.example.univer.dao.mapper.TeacherMapper;
import org.example.univer.dao.models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcTeacher implements DaoTeacherInterfaces {
    private final JdbcTemplate jdbcTemplate;
    private TeacherMapper teacherMapper;
    private static final String SQL_CREATE = "INSERT INTO teacher (firstName, lastName, gender, address, email, phone, birthday, cathedra_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String SQL_UPDATE = "UPDATE teacher SET firstName=?, lastName=?, gender=?, address=?, email=?, phone=?, birthday=?, cathedra_id=? WHERE id = ?";
    private static final String SQL_DELETE = "DELETE FROM teacher WHERE id = ?";
    private static final String SQL_GET_BY_ID = "SELECT * FROM teacher WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM teacher ORDER BY id";
    private static final String SQL_TEACHER_APPOINT_SUBJECT = "INSERT INTO teacher_subject (teacher_id, subject_id) VALUES (?, ?)";
    private static final String SQL_DELETE_TEACHER_TO_SUBJECT = "DELETE FROM teacher_subject WHERE teacher_id = ? AND subject_id = ?";

    @Autowired
    public JdbcTeacher(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public void create(Teacher teacher) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setString(1, teacher.getFirstName());
            ps.setString(2, teacher.getLastName());
            ps.setString(3, teacher.getGender());
            ps.setString(4, teacher.getAddres());
            ps.setString(5, teacher.getEmail());
            ps.setString(6, teacher.getPhone());
            ps.setObject(7, teacher.getBirthday());
            ps.setLong(8, teacher.getCathedra().getId());

            return ps;
        }, keyHolder);
    }

    public void teacherAppointSubject(Long teacher_id, Long subject_id) {
        if (Objects.isNull(teacher_id)) {
            throw new IllegalArgumentException("Учитель с таким id не существует или не найден!");
        }
        if (Objects.isNull(subject_id)) {
            throw new IllegalArgumentException("Предмет с таким id не существует или не найден!");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_TEACHER_APPOINT_SUBJECT);
            ps.setLong(1, teacher_id);
            ps.setLong(2, subject_id);
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Teacher teacher) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setString(1, teacher.getFirstName());
            ps.setString(2, teacher.getLastName());
            ps.setString(3, teacher.getGender());
            ps.setString(4, teacher.getAddres());
            ps.setString(5, teacher.getEmail());
            ps.setString(6, teacher.getPhone());
            ps.setObject(7, teacher.getBirthday());
            ps.setLong(8, teacher.getCathedra().getId());
            ps.setLong(9, teacher.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    public void deleteTeacherToSubject(Long teacher_id, Long subject_id) {
        jdbcTemplate.update(SQL_DELETE_TEACHER_TO_SUBJECT, teacher_id, subject_id);
    }

    @Override
    public Teacher findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, teacherMapper, id);
    }

    @Override
    public List<Teacher> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, teacherMapper);
    }
}
