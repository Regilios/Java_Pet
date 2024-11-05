package org.example.univer.dao.mapper;

import org.example.univer.dao.models.Cathedra;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class CathedraMapper implements RowMapper<Cathedra> {
    @Override
    public Cathedra mapRow(ResultSet rs, int rowNum) throws SQLException {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(rs.getLong("id"));
        cathedra.setName(rs.getString("name"));
        return cathedra;
    }
}
