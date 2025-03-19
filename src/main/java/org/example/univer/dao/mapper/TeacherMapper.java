package org.example.univer.dao.mapper;

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
    @Override
    public Teacher mapRow(ResultSet rs, int rowNum) throws SQLException {
        Teacher teacher = new Teacher();
        teacher.setId(rs.getLong("id"));
        teacher.setFirstName(rs.getString("firstName"));
        teacher.setLastName(rs.getString("lastName"));
        teacher.setGender(Gender.valueOf(rs.getString("gender")));
        teacher.setAddress(rs.getString("address"));
        teacher.setEmail(rs.getString("email"));
        teacher.setPhone(rs.getString("phone"));
        teacher.setBirthday(rs.getObject("birthday", LocalDate.class));

        Long cathedraId = rs.getLong("cathedra_id");
        if (!rs.wasNull()) {
            Cathedra cathedra = new Cathedra();
            cathedra.setId(cathedraId);
            cathedra.setName(rs.getString("cathedra_name"));
            teacher.setCathedra(cathedra);
        } else {
            teacher.setCathedra(null);
        }
        return teacher;
    }
}
