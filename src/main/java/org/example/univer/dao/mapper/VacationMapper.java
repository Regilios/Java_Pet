package org.example.univer.dao.mapper;

import org.example.univer.dao.jdbc.JdbcTeacher;
import org.example.univer.models.Teacher;
import org.example.univer.models.Vacation;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class VacationMapper implements RowMapper<Vacation> {
    private JdbcTeacher jdbcTeacher;

    public VacationMapper(JdbcTeacher jdbcTeacher) {
        this.jdbcTeacher = jdbcTeacher;
    }

    @Override
    public Vacation mapRow(ResultSet rs, int rowNum) throws SQLException {
        Vacation vacation = new Vacation();
        vacation.setId(rs.getLong("id"));
        vacation.setStartJob(rs.getDate("startjob").toLocalDate());
        vacation.setEndJob(rs.getDate("endjob").toLocalDate());

        Long localTeacherId = rs.getLong("teacher_id");
        if (!rs.wasNull()) {
            Teacher teacher = jdbcTeacher.findById(localTeacherId);
            vacation.setTeacher(teacher);
        }
        return vacation;
    }
}
