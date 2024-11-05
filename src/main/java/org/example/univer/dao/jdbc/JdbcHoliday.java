package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoHolidayInterfaces;
import org.example.univer.dao.mapper.HolidayMapper;
import org.example.univer.dao.mapper.VacationMapper;
import org.example.univer.dao.models.Holiday;
import org.example.univer.dao.models.Vacation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.util.List;

@Component
public class JdbcHoliday implements DaoHolidayInterfaces {
    private static final String SQL_FIND_ALL = "SELECT * FROM holiday ORDER BY id";
    private static final String SQL_GET_BY_ID = "SELECT * FROM holiday WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO holiday (description, start_holiday, end_holiday) VALUES (?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM holiday WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE holiday SET description=?, start_holiday=?, end_holiday=? WHERE id=?";

    private final JdbcTemplate jdbcTemplate;
    private HolidayMapper holidayMapper;

    @Autowired
    public JdbcHoliday(JdbcTemplate jdbcTemplate, HolidayMapper holidayMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.holidayMapper = holidayMapper;
    }

    @Override
    public void create(Holiday holiday) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setString(1, holiday.getDesc());
            ps.setObject(2, holiday.getStartHolidayLocal());
            ps.setObject(3, holiday.getEndHolidayLocal());
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Holiday holiday) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setString(1, holiday.getDesc());
            ps.setObject(2, holiday.getStartHolidayLocal());
            ps.setObject(3, holiday.getEndHolidayLocal());
            ps.setLong(4, holiday.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Holiday findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, holidayMapper, id);
    }

    @Override
    public List<Holiday> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, holidayMapper);
    }
}
