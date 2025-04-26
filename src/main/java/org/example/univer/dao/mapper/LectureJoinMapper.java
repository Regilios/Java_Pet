package org.example.univer.dao.mapper;

import org.example.univer.models.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LectureJoinMapper implements RowMapper<Lecture> {
    @Override
    public Lecture mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lecture lecture = new Lecture();
        lecture.setId(rs.getLong("lecture_id"));

        Cathedra cathedra = new Cathedra();
        cathedra.setId(rs.getLong("cathedra_id"));
        cathedra.setName(rs.getString("cathedra_name"));
        lecture.setCathedra(cathedra);

        Teacher teacher = new Teacher();
        teacher.setId(rs.getLong("teacher_id"));
        teacher.setFirstName(rs.getString("teacher_first_name"));
        teacher.setLastName(rs.getString("teacher_last_name"));
        lecture.setTeacher(teacher);

        Subject subject = new Subject();
        subject.setId(rs.getLong("subject_id"));
        subject.setName(rs.getString("subject_name"));
        lecture.setSubject(subject);

        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(rs.getLong("lecture_time_id"));
        lectureTime.setStart_lection(rs.getTimestamp("start_lection").toLocalDateTime());
        lectureTime.setEnd_lection(rs.getTimestamp("end_lection").toLocalDateTime());
        lecture.setTime(lectureTime);

        Audience audience = new Audience();
        audience.setId(rs.getLong("audience_id"));
        audience.setRoom(rs.getInt("room_number"));
        audience.setCapacity(rs.getInt("capacity"));
        lecture.setAudience(audience);

        return lecture;
    }
}

