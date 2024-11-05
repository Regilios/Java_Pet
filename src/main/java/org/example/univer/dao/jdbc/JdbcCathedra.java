package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoCathedraInterfaces;
import org.example.univer.dao.mapper.CathedraMapper;
import org.example.univer.dao.models.Cathedra;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class JdbcCathedra implements DaoCathedraInterfaces {
    private static final String SQL_GET_BY_ID = "SELECT * FROM cathedra WHERE id = ?";
    private static final String SQL_FIND_ALL = "SELECT * FROM cathedra";
    private static final String SQL_CREATE = "INSERT INTO cathedra (name) VALUES (?)";
    private static final String SQL_UPDATE = "UPDATE cathedra SET name=? WHERE id=?";
    private static final String SQL_DELETE = "DELETE FROM cathedra WHERE id = ?";

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
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setString(1, cathedra.getName());
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Cathedra cathedra) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setString(1, cathedra.getName());
            ps.setLong(2, cathedra.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Cathedra findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, cathedraMapper, id);
    }

    @Override
    public List<Cathedra> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, cathedraMapper);
    }


}
