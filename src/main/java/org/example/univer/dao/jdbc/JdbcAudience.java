package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoAudienceInterface;
import org.example.univer.dao.mapper.AudienceMapper;
import org.example.univer.models.Audience;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class JdbcAudience implements DaoAudienceInterface {
    private static final String GET_BY_ID = "SELECT * FROM audience WHERE id=?";
    private static final String SELECT_BY_PAGE = "SELECT * FROM audience LIMIT ? OFFSET ?";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM audience";
    private static final String FIND_ROOM = "SELECT COUNT(*) FROM audience WHERE room_number=?";
    private static final String FIND_ALL = "SELECT * FROM audience ORDER BY id";
    private static final String CREATE_AUDEINCE = "INSERT INTO audience (room_number, capacity) VALUES (?, ?)";
    private static final String UPDATE_AUDEINCE = "UPDATE audience SET room_number=?, capacity=? WHERE id=?";
    private static final String DELETE_AUDEINCE = "DELETE FROM audience WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private AudienceMapper audienceMapper;

    @Autowired
    public JdbcAudience(JdbcTemplate jdbcTemplate, AudienceMapper audienceMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.audienceMapper = audienceMapper;
    }

    @Override
    public void create(Audience audience) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_AUDEINCE, new String[]{"id"});
            ps.setInt(1, audience.getRoom());
            ps.setInt(2, audience.getCapacity());
            return ps;
        }, keyHolder);
        audience.setId((long) keyHolder.getKeyList().get(0).get("id"));
    }

    @Override
    public void update(Audience audience) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_AUDEINCE);
            ps.setInt(1, audience.getRoom());
            ps.setInt(2, audience.getCapacity());
            ps.setLong(3, audience.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_AUDEINCE, id);
    }

    @Override
    public Audience findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, audienceMapper, id);
    }

    @Override
    public List<Audience> findAll() {
        return jdbcTemplate.query(FIND_ALL, audienceMapper);
    }

    @Override
    public Page<Audience> findPaginatedAudience(Pageable pageable) {
        int total = jdbcTemplate.queryForObject(COUNT_ALL, Integer.class);
        List<Audience> audiences = jdbcTemplate.query(SELECT_BY_PAGE, audienceMapper, pageable.getPageSize(), pageable.getOffset());

        return new PageImpl<>(audiences, pageable, total);
    }

    @Override
    public boolean isSingle(Audience audience) {
        Integer result = jdbcTemplate.queryForObject(FIND_ROOM, Integer.class, audience.getRoom());
        return result != null && result > 0;
    }
}
