package org.example.univer.dao.mapper;

import org.example.univer.dao.models.Audience;
import org.example.univer.dao.models.Group;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
@Component
public class AudienceMapper implements RowMapper<Audience> {
    @Override
    public Audience mapRow(ResultSet rs, int rowNum) throws SQLException {
        Audience audience = new Audience();
        audience.setId(rs.getLong("id"));
        audience.setRoom(rs.getInt("room"));
        audience.setSize(rs.getInt("size"));
        return audience;
    }
}
