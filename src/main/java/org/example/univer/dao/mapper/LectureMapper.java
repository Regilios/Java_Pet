package org.example.univer.dao.mapper;

import org.example.univer.dao.jdbc.*;
import org.example.univer.models.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LectureMapper implements RowMapper<Lecture> {
    private JdbcCathedra jdbcCathedra;
    private JdbcTeacher jdbcTeacher;
    private JdbcSubject jdbcSubject;
    private JdbcLectureTime jdbcLectureTime;
    private JdbcAudience jdbcAudience;

    public LectureMapper(JdbcCathedra jdbcCathedra, JdbcTeacher jdbcTeacher, JdbcSubject jdbcSubject, JdbcLectureTime jdbcLectureTime, JdbcAudience jdbcAudience) {
        this.jdbcCathedra = jdbcCathedra;
        this.jdbcTeacher = jdbcTeacher;
        this.jdbcSubject = jdbcSubject;
        this.jdbcLectureTime = jdbcLectureTime;
        this.jdbcAudience = jdbcAudience;
    }

    @Override
    public Lecture mapRow(ResultSet rs, int rowNum) throws SQLException {
        Lecture lecture = new Lecture();
        lecture.setId(rs.getLong("id"));

        Long localCathedraId = rs.getLong("cathedra_id");
        Long localTeacherId = rs.getLong("teacher_id");
        Long localSubjectId = rs.getLong("subject_id");
        Long localLectureTimeId = rs.getLong("lecture_time_id");
        Long localAudienceId = rs.getLong("audience_id");

        if (!rs.wasNull()) {
            Cathedra cathedra = jdbcCathedra.findById(localCathedraId);
            lecture.setCathedra(cathedra);
        }
        if (!rs.wasNull()) {
            Teacher teacher = jdbcTeacher.findById(localTeacherId);
            lecture.setTeacher(teacher);
        }
        if (!rs.wasNull()) {
            Subject subject = jdbcSubject.findById(localSubjectId);
            lecture.setSubject(subject);
        }
        if (!rs.wasNull()) {
            LectureTime lectureTime = jdbcLectureTime.findById(localLectureTimeId);
            lecture.setTime(lectureTime);
        }
        if (!rs.wasNull()) {
            Audience audience = jdbcAudience.findById(localAudienceId);
            lecture.setAudience(audience);
        }

        return lecture;
    }
}
