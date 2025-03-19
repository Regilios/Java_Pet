package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoCathedraInterface;
import org.example.univer.dao.mapper.CathedraMapper;
import org.example.univer.models.Cathedra;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class JdbcCathedra implements DaoCathedraInterface {
    private static final String GET_BY_ID = "SELECT * FROM cathedra WHERE id=?";
    private static final String FIND_ALL = "SELECT * FROM cathedra ORDER BY id";
    private static final String CREATE_CATHEDRA = "INSERT INTO cathedra (name) VALUES (?)";
    private static final String UPDATE_CATHEDRA = "UPDATE cathedra SET name=? WHERE id=?";
    private static final String DELETE_CATHEDRA = "DELETE FROM cathedra WHERE id=?";
    private static final String FIND_CATHEDRA = "SELECT COUNT(*) FROM cathedra WHERE name=?";

    private final JdbcTemplate jdbcTemplate;
    private CathedraMapper cathedraMapper;

    @Autowired
    public JdbcCathedra(JdbcTemplate jdbcTemplate, CathedraMapper cathedraMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.cathedraMapper = cathedraMapper;
    }

    @Override
    public void create(Cathedra cathedra) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_CATHEDRA, new String[]{"id"});
            ps.setString(1, cathedra.getName());
            return ps;
        }, keyHolder);
        cathedra.setId((long) keyHolder.getKeyList().get(0).get("id"));
    }

    @Override
    public void update(Cathedra cathedra) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_CATHEDRA);
            ps.setString(1, cathedra.getName());
            ps.setLong(2, cathedra.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_CATHEDRA, id);
    }

    @Override
    public Cathedra findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, cathedraMapper, id);
    }

    @Override
    public List<Cathedra> findAll() {
        return jdbcTemplate.query(FIND_ALL, cathedraMapper);
    }

    @Override
    public boolean isSingle(Cathedra cathedra) {
        Integer result = jdbcTemplate.queryForObject(FIND_CATHEDRA, Integer.class, cathedra.getName());
        return result != null && result > 0;
    }
}
