package org.example.univer.dao.mapper;

import org.example.univer.models.Cathedra;
import org.example.univer.models.Group;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class GroupMapper implements RowMapper<Group> {
    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();
        group.setId(rs.getLong("id"));
        group.setName(rs.getString("name"));

        Long cathedraId = rs.getLong("cathedra_id");
        if (!rs.wasNull()) {
            Cathedra cathedra = new Cathedra();
            cathedra.setId(cathedraId);
            cathedra.setName(rs.getString("cathedra_name"));
            group.setCathedra(cathedra);
        } else {
            group.setCathedra(null);
        }

        return group;
    }
}
