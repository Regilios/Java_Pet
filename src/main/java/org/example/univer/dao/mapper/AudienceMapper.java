package org.example.univer.dao.mapper;

import org.example.univer.models.Audience;
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
        audience.setRoom(rs.getInt("room_number"));
        audience.setCapacity(rs.getInt("capacity"));
        return audience;
    }
}
