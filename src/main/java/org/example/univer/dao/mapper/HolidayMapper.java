package org.example.univer.dao.mapper;

import org.example.univer.models.Holiday;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Objects;

@Component
public class HolidayMapper implements RowMapper<Holiday> {
    @Override
    public Holiday mapRow(ResultSet rs, int rowNum) throws SQLException {
        Holiday holiday = new Holiday();
        holiday.setId(rs.getLong("id"));
        holiday.setDesc(rs.getString("description"));

        Date holidayStart = rs.getDate("start_holiday");
        if (Objects.nonNull(holidayStart)) {
            holiday.setStartHoliday(holidayStart.toLocalDate());
        }

        Date holidayEnd = rs.getDate("end_holiday");
        if (Objects.nonNull(holidayEnd)) {
            holiday.setEndHoliday(holidayEnd.toLocalDate());
        }
        return holiday;
    }
}
