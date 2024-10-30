package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoVacationInterfaces;
import org.example.univer.dao.mapper.VacationMapper;
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
public class JdbcVacation implements DaoVacationInterfaces {
    private static final String SQL_FIND_ALL = "SELECT * FROM vacation ORDER BY id";
    private static final String SQL_GET_BY_ID = "SELECT * FROM vacation WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO vacation (startjob, endjob, teacher_id) VALUES (?, ?, ?)";
    private static final String SQL_DELETE = "DELETE FROM vacation WHERE id = ?";
    private static final String SQL_UPDATE = "UPDATE vacation SET startjob=?, endjob=?, teacher_id=? WHERE id=?";
    private final JdbcTemplate jdbcTemplate;
    private VacationMapper vacationMapper;

    @Autowired
    public JdbcVacation(JdbcTemplate jdbcTemplate, VacationMapper vacationMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.vacationMapper = vacationMapper;
    }

    @Override
    public void create(Vacation vacation) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setDate(1, Date.valueOf(vacation.getStartJob()));
            ps.setDate(2, Date.valueOf(vacation.getEndJob()));
            ps.setLong(3, vacation.getTeacher().getId());
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Vacation vacation) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setDate(1, Date.valueOf(vacation.getStartJob()));
            ps.setDate(2, Date.valueOf(vacation.getEndJob()));
            ps.setLong(3, vacation.getTeacher().getId());
            ps.setLong(4, vacation.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    @Override
    public Vacation findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, vacationMapper, id);
    }

    @Override
    public List<Vacation> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, vacationMapper);
    }
}
