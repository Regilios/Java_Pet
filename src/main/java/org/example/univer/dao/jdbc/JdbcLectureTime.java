package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoLectureTimeInterface;
import org.example.univer.dao.mapper.LectureTimeMapper;
import org.example.univer.models.LectureTime;
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
public class JdbcLectureTime implements DaoLectureTimeInterface {
    private static final String CREATE_LECTION_TIME = "INSERT INTO lectionTime (start_lection, end_lection) VALUES (?, ?)";
    private static final String DELETE_LECTION_TIME = "DELETE FROM lectionTime WHERE id = ?";
    private static final String UPDATE_LECTION_TIME = "UPDATE lectionTime SET start_lection=?, end_lection=? WHERE id=?";
    private static final String FIND_ALL = "SELECT * FROM lectionTime ORDER BY id";
    private static final String FIND_LECTIONTIME = "SELECT COUNT(*) FROM lectionTime WHERE start_lection=? AND end_lection=?";
    private static final String GET_BY_ID = "SELECT * FROM lectionTime WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private LectureTimeMapper lectureTimeMapper;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Autowired
    public JdbcLectureTime(JdbcTemplate jdbcTemplate, LectureTimeMapper lectureTimeMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lectureTimeMapper = lectureTimeMapper;
    }

    @Override
    public void create(LectureTime lectureTime) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_LECTION_TIME, new String[]{"id"});
            ps.setObject(1, LocalDateTime.parse(lectureTime.getStart_lection(), formatter));
            ps.setObject(2, LocalDateTime.parse(lectureTime.getEnd_lection(), formatter));
            return ps;
        }, keyHolder);
        lectureTime.setId((long) keyHolder.getKeyList().get(0).get("id"));
    }

    @Override
    public void update(LectureTime lectureTime) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_LECTION_TIME);
            ps.setObject(1, LocalDateTime.parse(lectureTime.getStart_lection(), formatter));
            ps.setObject(2, LocalDateTime.parse(lectureTime.getEnd_lection(), formatter));
            ps.setLong(3, lectureTime.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_LECTION_TIME, id);
    }

    @Override
    public LectureTime findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, lectureTimeMapper, id);
    }

    @Override
    public List<LectureTime> findAll() {
        return jdbcTemplate.query(FIND_ALL, lectureTimeMapper);
    }

    @Override
    public boolean isSingle(LectureTime lectureTime) {
        Integer result = jdbcTemplate.queryForObject(FIND_LECTIONTIME, Integer.class, lectureTime.getStartLocal(), lectureTime.getEndLocal());
        return result != null && result > 0;
    }
}
