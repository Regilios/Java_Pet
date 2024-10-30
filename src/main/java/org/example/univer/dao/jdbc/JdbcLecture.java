package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoLectureInterfaces;
import org.example.univer.dao.mapper.LectureMapper;
import org.example.univer.dao.models.Lecture;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
@Component
public class JdbcLecture implements DaoLectureInterfaces {
    private static final String SQL_FIND_ALL = "SELECT * FROM lection ORDER BY id";
    private static final String SQL_GET_BY_ID = "SELECT * FROM lection WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (?, ?, ?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM lection WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE lection SET cathedra_id=?, teacher_id=?, subject_id=?, lecture_time_id=?, audience_id=? WHERE id=?";
    private static final String SQL_FIND_LECTION_BY_GROUP = "SELECT * FROM lection WHERE id IN (SELECT lection_id FROM group_lection WHERE group_id = ?)";
    private static final String SQL_FIND_LECTION_FOR_TEACHER = "SELECT * FROM lection WHERE teacher_id = ?";

    private final JdbcTemplate jdbcTemplate;
    private LectureMapper lectureMapper;

    @Autowired
    public JdbcLecture(JdbcTemplate jdbcTemplate, LectureMapper lectureMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lectureMapper = lectureMapper;
    }

    @Override
    public void create(Lecture lecture) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setLong(1, lecture.getCathedra().getId());
            ps.setLong(2, lecture.getTeacher().getId());
            ps.setLong(3, lecture.getSubject().getId());
            ps.setLong(4, lecture.getTime().getId());
            ps.setLong(5, lecture.getAudience().getId());
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Lecture lecture) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setLong(1, lecture.getId());
            ps.setLong(2, lecture.getCathedra().getId());
            ps.setLong(3, lecture.getTeacher().getId());
            ps.setLong(4, lecture.getSubject().getId());
            ps.setLong(5, lecture.getTime().getId());
            ps.setLong(6, lecture.getAudience().getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Lecture findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, lectureMapper, id);
    }

    @Override
    public List<Lecture> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, lectureMapper);
    }

    public List<Lecture> findLectionByGroupIdForStudent(Long id) {
        // устаревшйи вариант
        // return jdbcTemplate.query(SQL_FIND_LECTION_BY_GROUP, new Object[]{id}, lectureMapper);
        return jdbcTemplate.query(SQL_FIND_LECTION_BY_GROUP, ps -> ps.setLong(1, id), lectureMapper);
    }

    public List<Lecture> findLectionByGroupIdForTeacher(Long id) {
        // устаревшйи вариант
        // return jdbcTemplate.query(SQL_FIND_LECTION_BY_GROUP, new Object[]{id}, lectureMapper);
        return jdbcTemplate.query(SQL_FIND_LECTION_FOR_TEACHER, ps -> ps.setLong(1, id), lectureMapper);
    }
}
