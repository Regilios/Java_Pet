package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoSubjectInterface;
import org.example.univer.dao.mapper.SubjectMapper;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class JdbcSubject implements DaoSubjectInterface {
    private static final String CREATE_SUBJECT = "INSERT INTO subject (name, description) VALUES (?, ?)";
    private static final String DELETE_SUBJECT = "DELETE FROM subject WHERE id=?";
    private static final String UPDATE_SUBJECT = "UPDATE subject SET name=?, description=? WHERE id=?";
    private static final String GET_BY_ID = "SELECT * FROM subject WHERE id=?";
    private static final String FIND_ALL = "SELECT * FROM subject ORDER BY id";
    private static final String FIND_SUBJECT = "SELECT COUNT(*) FROM subject WHERE name=?";
    private static final String CHECK_TEACHER_ASSIGNED_SUBJECT = "SELECT COUNT(*) FROM teacher_subject WHERE teacher_id=? AND subject_id=?";
    private static final String FIND_SUBJECTS_BY_TEACHER_ID = "SELECT s.* FROM subject s INNER JOIN teacher_subject ts ON s.id = ts.subject_id WHERE ts.teacher_id = teacher_id";

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
            PreparedStatement ps = connection.prepareStatement(CREATE_SUBJECT, new String[]{"id"});
            ps.setString(1, subject.getName());
            ps.setString(2, subject.getDescription());
            return ps;
        }, keyHolder);
        subject.setId((long) keyHolder.getKeyList().get(0).get("id"));
    }

    @Override
    public void update(Subject subject) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_SUBJECT);
            ps.setString(1, subject.getName());
            ps.setString(2, subject.getDescription());
            ps.setLong(3, subject.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_SUBJECT, id);
    }

    @Override
    public Subject findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, subjectMapper, id);
    }

    @Override
    public List<Subject> findAll() {
        return jdbcTemplate.query(FIND_ALL, subjectMapper);
    }

//    public List<Subject> getSubjectById(List<Long> subjectIds) {
//        if (subjectIds == null || subjectIds.isEmpty()) {
//            throw new IllegalArgumentException("The list of subject IDs cannot be null or empty");
//        }
//
//        String ids = subjectIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
//        String FIND_ALL_BY_ID = "SELECT * FROM subject WHERE id IN ("+ ids +")";
//
//        return jdbcTemplate.query(FIND_ALL_BY_ID, subjectMapper);
//    }

    public List<Subject> getSubjectById(Long teacher_id) {
        if (teacher_id == null) {
            throw new IllegalArgumentException("The teacher_id cannot be null");
        }
        return jdbcTemplate.query(FIND_SUBJECTS_BY_TEACHER_ID, subjectMapper);
    }

    @Override
    public boolean checkTeacherAssignedSubject(Teacher teacher, Subject subject) {
        Integer result = jdbcTemplate.queryForObject(CHECK_TEACHER_ASSIGNED_SUBJECT, Integer.class, teacher.getId(), subject.getId());
        return result != null && result > 0;
    }

    @Override
    public boolean isSingle(Subject subject) {
        Integer result = jdbcTemplate.queryForObject(FIND_SUBJECT, Integer.class, subject.getName());
        return result != null && result > 0;
    }
}

