package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoSubjectInterfaces;
import org.example.univer.dao.mapper.SubjectMapper;
import org.example.univer.dao.models.Subject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
@Component
public class JdbcSubject implements DaoSubjectInterfaces {
    private static final String SQL_FIND_ALL = "SELECT * FROM subject ORDER BY id";
    private static final String SQL_GET_BY_ID = "SELECT * FROM subject WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO subject (name, description) VALUES (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM subject WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE subject SET name=?, description=? WHERE id=?";
    private final JdbcTemplate jdbcTemplate;
    private SubjectMapper subjectMapper;

    @Autowired
    public JdbcSubject(JdbcTemplate jdbcTemplate, SubjectMapper subjectMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.subjectMapper = subjectMapper;
    }

    @Override
    public void create(Subject subject) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setString(1, subject.getName());
            ps.setString(2, subject.getDescription());
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Subject subject) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setString(1, subject.getName());
            ps.setString(2, subject.getDescription());
            ps.setLong(3, subject.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Subject findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, subjectMapper, id);
    }

    @Override
    public List<Subject> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, subjectMapper);
    }
}

