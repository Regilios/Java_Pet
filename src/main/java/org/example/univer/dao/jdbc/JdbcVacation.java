package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoVacationInterface;
import org.example.univer.dao.mapper.VacationMapper;
import org.example.univer.models.Vacation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class JdbcVacation implements DaoVacationInterface {
    private static final String CREATE_VACATION = "INSERT INTO vacation (startjob, endjob, teacher_id) VALUES (?, ?, ?)";
    private static final String DELETE_VACATION = "DELETE FROM vacation WHERE id=?";
    private static final String UPDATE_VACATION = "UPDATE vacation SET startjob=?, endjob=?, teacher_id=? WHERE id=?";
    private static final String FIND_ALL = "SELECT v.*, t.firstName, t.lastName FROM vacation v INNER JOIN teacher t ON v.teacher_id = t.id";
    private static final String FIND_VACATION = "SELECT COUNT(*) FROM vacation WHERE startjob=? AND endjob=? AND teacher_id=?";
    private static final String GET_BY_ID = "SELECT v.*, t.firstName, t.lastName FROM vacation v INNER JOIN teacher t ON v.teacher_id = t.id WHERE v.id=?";
    private static final String GET_BY_TEACHER_ID = "SELECT v.*, t.firstName, t.lastName FROM vacation v INNER JOIN teacher t ON v.teacher_id = t.id WHERE  v.teacher_id=? ORDER BY id";

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
            PreparedStatement ps = connection.prepareStatement(CREATE_VACATION, new String[]{"id"});
            ps.setObject(1, vacation.getStartJob());
            ps.setObject(2, vacation.getEndJob());
            ps.setLong(3, vacation.getTeacher().getId());
            return ps;
        }, keyHolder);
        vacation.setId((long) keyHolder.getKeyList().get(0).get("id"));
    }

    @Override
    public void update(Vacation vacation) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_VACATION);
            ps.setObject(1, vacation.getStartJob());
            ps.setObject(2, vacation.getEndJob());
            ps.setLong(3, vacation.getTeacher().getId());
            ps.setLong(4, vacation.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_VACATION, id);
    }

    @Override
    public Vacation findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, vacationMapper, id);
    }


    public List<Vacation> findByTeacherId(Long id) {
        return jdbcTemplate.query(GET_BY_TEACHER_ID, vacationMapper, id);
    }

    @Override
    public List<Vacation> findAll() {
        return jdbcTemplate.query(FIND_ALL, vacationMapper);
    }

    @Override
    public boolean isSingle(Vacation vacation) {
        Integer result = jdbcTemplate.queryForObject(FIND_VACATION, Integer.class, vacation.getStartJob(), vacation.getEndJob(), vacation.getTeacher().getId());
        return result != null && result > 0;
    }
}
