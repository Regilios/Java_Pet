package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoAudienceInterfaces;
import org.example.univer.dao.mapper.AudienceMapper;
import org.example.univer.dao.models.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class JdbcAudience implements DaoAudienceInterfaces {
    private final JdbcTemplate jdbcTemplate;
    private AudienceMapper audienceMapper;
    private static final String SQL_GET_BY_ID = "SELECT * FROM audience WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM audience";
    private static final String SQL_CREATE = "INSERT INTO audience (room, size) VALUES (?, ?)";
    private static final String SQL_UPDATE = "UPDATE audience SET room=?, size=? WHERE id=?";
    private static final String SQL_DELETE = "DELETE FROM audience WHERE id = ?";

    @Autowired
    public JdbcAudience(JdbcTemplate jdbcTemplate, AudienceMapper audienceMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.audienceMapper = audienceMapper;
    }

    @Override
    public void create(Audience audience) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setInt(1, audience.getRoom());
            ps.setInt(2, audience.getSize());
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Audience audience) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setInt(1, audience.getRoom());
            ps.setInt(2, audience.getSize());
            ps.setLong(3, audience.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Audience findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, audienceMapper, id);
    }

    @Override
    public List<Audience> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, audienceMapper);
    }
}
