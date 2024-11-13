package org.example.univer.dao.mapper;

import org.example.univer.dao.jdbc.JdbcCathedra;
import org.example.univer.models.Cathedra;
import org.example.univer.models.Gender;
import org.example.univer.models.Teacher;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class TeacherMapper implements RowMapper<Teacher> {
    private JdbcCathedra jdbcCathedra;

    public TeacherMapper(JdbcCathedra jdbcCathedra) {
        this.jdbcCathedra = jdbcCathedra;
    }

    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getLong("id"));
        teacher.setFirstName(rs.getString("firstName"));
        teacher.setLastName(rs.getString("lastName"));
        teacher.setGender(Gender.valueOf(rs.getString("gender")));
        teacher.setAddres(rs.getString("address"));
        teacher.setEmail(rs.getString("email"));
        teacher.setPhone(rs.getString("phone"));
        teacher.setBirthday(rs.getObject("birthday", LocalDate.class));

        Long localCathderaId = rs.getLong("cathedra_id");
        if (!rs.wasNull()) {
            Cathedra cathedra = jdbcCathedra.findById(localCathderaId);
            teacher.setCathedra(cathedra);
        }
        return teacher;
    }
}
