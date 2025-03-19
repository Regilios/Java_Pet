package org.example.univer.dao.mapper;

import org.example.univer.models.Gender;
import org.example.univer.models.Group;
import org.example.univer.models.Student;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

@Component
public class StudentMapper implements RowMapper<Student> {

    public Student mapRow(ResultSet rs, int rowNum) throws SQLException {
        Student student = new Student();
        student.setId(rs.getLong("id"));
        student.setFirstName(rs.getString("firstName"));
        student.setLastName(rs.getString("lastName"));
        student.setGender(Gender.valueOf(rs.getString("gender")));
        student.setAddress(rs.getString("address"));
        student.setEmail(rs.getString("email"));
        student.setPhone(rs.getString("phone"));
        student.setBirthday(rs.getObject("birthday", LocalDate.class));

        Long localGroupId = rs.getLong("group_id");

        if (!rs.wasNull()) {
            Group group = new Group();
            group.setId(localGroupId);
            group.setName(rs.getString("group_name"));
            student.setGroup(group);
        } else {
            student.setGroup(null);
        }

        return student;

    }
}
