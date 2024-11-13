package org.example.univer.dao.mapper;

import org.example.univer.models.Holiday;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class HolidayMapper implements RowMapper<Holiday> {
    @Override
    public Holiday mapRow(ResultSet rs, int rowNum) throws SQLException {
        Holiday holiday = new Holiday();
        holiday.setId(rs.getLong("id"));
        holiday.setDesc(rs.getString("description"));
        holiday.setStartHoliday(rs.getDate("start_holiday").toLocalDate());
        holiday.setEndHoliday(rs.getDate("end_holiday").toLocalDate());
        return holiday;
    }
}
