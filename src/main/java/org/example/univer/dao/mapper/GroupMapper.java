package org.example.univer.dao.mapper;

import org.example.univer.dao.jdbc.JdbcCathedra;
import org.example.univer.dao.jdbc.JdbcGroup;
import org.example.univer.dao.models.Cathedra;
import org.example.univer.dao.models.Group;
import org.example.univer.dao.models.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

@Component
public class GroupMapper implements RowMapper<Group> {
    private JdbcCathedra jdbcCathedra;

    public GroupMapper(JdbcCathedra jdbcCathedra) {
        this.jdbcCathedra = jdbcCathedra;
    }

    @Override
    public Group mapRow(ResultSet rs, int rowNum) throws SQLException {
        Group group = new Group();
        group.setId(rs.getLong("id"));
        group.setNameGroup(rs.getString("name"));

        Long localCathedraId = rs.getLong("cathedra_id");
        if(!rs.wasNull()) {
            Cathedra cathedra = jdbcCathedra.findById(localCathedraId);
            group.setCathedra(cathedra);
        }
        return group;
    }
}
