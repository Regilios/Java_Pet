package org.example.univer.dao.mapper;

import org.example.univer.dao.jdbc.*;
import org.example.univer.dao.models.*;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.SQLException;

@Component
public class LectureMapper implements RowMapper<Lecture> {
    private JdbcCathedra jdbcCathedra;
    private JdbcTeacher jdbcTeacher;
    private JdbcSubject jdbcSubject;
    private JdbsLectureTime jdbsLectureTime;
    private JdbcAudience jdbcAudience;

    public LectureMapper(JdbcCathedra jdbcCathedra, JdbcTeacher jdbcTeacher, JdbcSubject jdbcSubject, JdbsLectureTime jdbsLectureTime, JdbcAudience jdbcAudience) {
        this.jdbcCathedra = jdbcCathedra;
        this.jdbcTeacher = jdbcTeacher;
        this.jdbcSubject = jdbcSubject;
        this.jdbsLectureTime = jdbsLectureTime;
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

            Teacher teacher = jdbcTeacher.findById(localTeacherId);
            lecture.setTeacher(teacher);

            Subject subject = jdbcSubject.findById(localSubjectId);
            lecture.setSubject(subject);

            LectureTime lectureTime = jdbsLectureTime.findById(localLectureTimeId);
            lecture.setTime(lectureTime);

            Audience audience = jdbcAudience.findById(localAudienceId);
            lecture.setAudience(audience);
        }

        return lecture;
    }
}
