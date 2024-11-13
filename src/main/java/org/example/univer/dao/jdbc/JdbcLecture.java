package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoLectureInterface;
import org.example.univer.dao.mapper.LectureMapper;
import org.example.univer.models.Lecture;
import org.example.univer.models.Student;
import org.example.univer.models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;

@Component
public class JdbcLecture implements DaoLectureInterface {
    private static final String FIND_ALL = "SELECT * FROM lection ORDER BY id";
    private static final String GET_BY_ID = "SELECT * FROM lection WHERE id = ?";
    private static final String CREATE_LECTURE = "INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_LECTURE = "DELETE FROM lection WHERE id = ?";
    private static final String UPDATE_LECTURE = "UPDATE lection SET cathedra_id=?, teacher_id=?, subject_id=?, lecture_time_id=?, audience_id=? WHERE id=?";
    private static final String GET_TIMETEBALE_TEACHER = "SELECT t1.* FROM lection t1 JOIN lectiontime t2 ON t1.lecture_time_id = t2.id WHERE t1.teacher_id = ? AND EXTRACT(DAY FROM t2.start_lection) = ? AND EXTRACT(MONTH FROM t2.start_lection) = ?";
    private static final String GET_TIMETEBALE_STUDENT = "SELECT t1.* FROM lection t1 JOIN lectiontime t2 ON t1.lecture_time_id = t2.id JOIN group_lection g ON t1.id = g.lection_id WHERE g.group_id = ? AND EXTRACT(DAY FROM t2.start_lection) = ? AND EXTRACT(MONTH FROM t2.start_lection) = ?";

    private final JdbcTemplate jdbcTemplate;
    private LectureMapper lectureMapper;

    @Autowired
    public JdbcLecture(JdbcTemplate jdbcTemplate, LectureMapper lectureMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lectureMapper = lectureMapper;
    }

    @Override
    public void create(Lecture lecture) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_LECTURE, new String[]{"id"});
            ps.setLong(1, lecture.getCathedra().getId());
            ps.setLong(2, lecture.getTeacher().getId());
            ps.setLong(3, lecture.getSubject().getId());
            ps.setLong(4, lecture.getTime().getId());
            ps.setLong(5, lecture.getAudience().getId());
            return ps;
        }, keyHolder);
        lecture.setId((long) keyHolder.getKeyList().get(0).get("id"));
    }

    @Override
    public void update(Lecture lecture) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_LECTURE);
            ps.setLong(1, lecture.getId());
            ps.setLong(2, lecture.getCathedra().getId());
            ps.setLong(3, lecture.getTeacher().getId());
            ps.setLong(4, lecture.getSubject().getId());
            ps.setLong(5, lecture.getTime().getId());
            ps.setLong(6, lecture.getAudience().getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_LECTURE, id);
    }

    @Override
    public Lecture findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, lectureMapper, id);
    }

    @Override
    public List<Lecture> findAll() {
        return jdbcTemplate.query(FIND_ALL, lectureMapper);
    }

    public List<Lecture> getTimetable(Student student, LocalDate localDate) {

        return jdbcTemplate.query(GET_TIMETEBALE_STUDENT, ps -> {
            ps.setLong(1, student.getGroup().getId());
            ps.setInt(2, localDate.getDayOfMonth());
            ps.setInt(3, localDate.getMonthValue());
        }, lectureMapper);
    }

    public List<Lecture> getTimetable(Teacher teacher, LocalDate localDate) {
        return jdbcTemplate.query(GET_TIMETEBALE_TEACHER, ps -> {
            ps.setLong(1, teacher.getId());
            ps.setInt(2, localDate.getDayOfMonth());
            ps.setInt(3, localDate.getMonthValue());
        }, lectureMapper);
    }
}
