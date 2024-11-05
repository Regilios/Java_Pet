package org.example.univer.dao.mapper;

import org.example.univer.dao.jdbc.JdbcGroup;
import org.example.univer.dao.models.Gender;
import org.example.univer.dao.models.Group;
import org.example.univer.dao.models.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class StudentMapper implements RowMapper<Student> {
    private JdbcGroup jdbcGroup;

    public StudentMapper(JdbcGroup jdbcGroup) {
        this.jdbcGroup = jdbcGroup;
    }

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setFirstName(rs.getString("firstName"));
        student.setLastName(rs.getString("lastName"));
        student.setGender(Gender.valueOf(rs.getString("gender")));
        student.setAddres(rs.getString("address"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setBirthday(rs.getObject("birthday", LocalDate.class));

        Long localGroupId = rs.getLong("group_id");

        if (!rs.wasNull()) {
            Group group = jdbcGroup.findById(localGroupId);
            student.setGroup(group);
        }
        return student;
    }
}
