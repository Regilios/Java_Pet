package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoHolidayInterface;
import org.example.univer.dao.mapper.HolidayMapper;
import org.example.univer.models.Holiday;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.LocalDateTime;
import java.util.List;

@Component
public class JdbcHoliday implements DaoHolidayInterface {
    private static final String FIND_ALL = "SELECT * FROM holiday ORDER BY id";
    private static final String FIND_HOLIDAY = "SELECT COUNT(*) FROM holiday WHERE description=?";
    private static final String FIND_HOLIDAY_BY_DATE = "SELECT COUNT(*) FROM holiday WHERE ? BETWEEN start_holiday AND end_holiday";
    private static final String GET_BY_ID = "SELECT * FROM holiday WHERE id=?";
    private static final String CREATE_HOLIDAY = "INSERT INTO holiday (description, start_holiday, end_holiday) VALUES (?, ?, ?)";
    private static final String DELETE_HOLIDAY = "DELETE FROM holiday WHERE id=?";
    private static final String UPDATE_HOLIDAY = "UPDATE holiday SET description=?, start_holiday=?, end_holiday=? WHERE id=?";

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
            PreparedStatement ps = connection.prepareStatement(CREATE_HOLIDAY, new String[]{"id"});
            ps.setString(1, holiday.getDesc());
            ps.setObject(2, holiday.getStart_holiday());
            ps.setObject(3, holiday.getEnd_holiday());
            return ps;
        }, keyHolder);
        holiday.setId((long) keyHolder.getKeyList().get(0).get("id"));
    }

    @Override
    public void update(Holiday holiday) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_HOLIDAY);
            ps.setString(1, holiday.getDesc());
            ps.setObject(2, holiday.getStart_holiday());
            ps.setObject(3, holiday.getEnd_holiday());
            ps.setLong(4, holiday.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_HOLIDAY, id);
    }

    @Override
    public Holiday findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, holidayMapper, id);
    }

    @Override
    public List<Holiday> findAll() {
        return jdbcTemplate.query(FIND_ALL, holidayMapper);
    }

    @Override
    public boolean isSingle(Holiday holiday) {
        Integer result = jdbcTemplate.queryForObject(FIND_HOLIDAY, Integer.class, holiday.getDesc());
        return result != null && result > 0;
    }

    @Override
    public boolean lectureDoesNotFallOnHoliday(LocalDateTime date) {
        Integer result = jdbcTemplate.queryForObject(FIND_HOLIDAY_BY_DATE, Integer.class, date);
        return result != null && result > 0;
    }
}
