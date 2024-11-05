package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoLectureTimeInterfaces;
import org.example.univer.dao.mapper.LectureTimeMapper;
import org.example.univer.dao.models.LectureTime;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
public class JdbsLectureTime implements DaoLectureTimeInterfaces {
    private static final String SQL_FIND_ALL = "SELECT * FROM lectionTime ORDER BY id";
    private static final String SQL_GET_BY_ID = "SELECT * FROM lectionTime WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO lectionTime (start_lection, end_lection) VALUES (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM lectionTime WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE lectionTime SET start_lection=?, end_lection=? WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private LectureTimeMapper lectureTimeMapper;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public JdbsLectureTime(JdbcTemplate jdbcTemplate, LectureTimeMapper lectureTimeMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lectureTimeMapper = lectureTimeMapper;
    }

    @Override
    public void create(LectureTime lectureTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setObject(1, LocalDateTime.parse(lectureTime.getStart(), formatter));
            ps.setObject(2, LocalDateTime.parse(lectureTime.getEnd(), formatter));
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(LectureTime lectureTime) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setObject(1, LocalDateTime.parse(lectureTime.getStart(), formatter));
            ps.setObject(2, LocalDateTime.parse(lectureTime.getEnd(), formatter));
            ps.setLong(3, lectureTime.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public LectureTime findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, lectureTimeMapper, id);
    }

    @Override
    public List<LectureTime> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, lectureTimeMapper);
    }
}
