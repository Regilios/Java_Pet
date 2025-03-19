package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoLectureInterface;
import org.example.univer.dao.mapper.LectureJoinMapper;
import org.example.univer.dao.mapper.LectureMapper;
import org.example.univer.models.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.time.LocalDate;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Component
public class JdbcLecture implements DaoLectureInterface {
    private static final String FIND_ALL = "SELECT * FROM lection ORDER BY id";
    private static final String SELECT_BY_PAGE = "SELECT l.id AS lecture_id,  l.cathedra_id, c.name AS cathedra_name, l.teacher_id, t.firstName AS teacher_first_name, t.lastName AS teacher_last_name, l.subject_id, s.name AS subject_name, l.lecture_time_id, lt.start_lection, lt.end_lection, l.audience_id, a.room_number, a.capacity FROM lection l LEFT JOIN cathedra c ON l.cathedra_id = c.id LEFT JOIN teacher t ON l.teacher_id = t.id LEFT JOIN subject s ON l.subject_id = s.id LEFT JOIN lectionTime lt ON l.lecture_time_id = lt.id LEFT JOIN audience a ON l.audience_id = a.id ORDER BY l.id LIMIT ? OFFSET ?";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM lection";
    private static final String FIND_ALL_AT_ONCE = "SELECT l.id AS lecture_id,  l.cathedra_id, c.name AS cathedra_name, l.teacher_id, t.firstName AS teacher_first_name, t.lastName AS teacher_last_name, l.subject_id, s.name AS subject_name, l.lecture_time_id, lt.start_lection, lt.end_lection, l.audience_id, a.room_number, a.capacity FROM lection l LEFT JOIN cathedra c ON l.cathedra_id = c.id LEFT JOIN teacher t ON l.teacher_id = t.id LEFT JOIN subject s ON l.subject_id = s.id LEFT JOIN lectionTime lt ON l.lecture_time_id = lt.id LEFT JOIN audience a ON l.audience_id = a.id ORDER BY l.id";
    private static final String FIND_LECTURE = "SELECT COUNT(*) FROM lection WHERE teacher_id=? AND subject_id=? AND lecture_time_id=? AND audience_id=?";
    private static final String GET_BY_ID = "SELECT * FROM lection WHERE id=?";
    private static final String CREATE_LECTURE = "INSERT INTO lection (cathedra_id, teacher_id, subject_id, lecture_time_id, audience_id) VALUES (?, ?, ?, ?, ?)";
    private static final String DELETE_LECTURE = "DELETE FROM lection WHERE id=?";
    private static final String UPDATE_LECTURE = "UPDATE lection SET cathedra_id=?, teacher_id=?, subject_id=?, lecture_time_id=?, audience_id=? WHERE id=?";
    private static final String GET_TIMETEBALE_TEACHER_FOR_CREATE = "SELECT t1.* FROM lection t1 JOIN lectiontime t2 ON t1.lecture_time_id = t2.id WHERE t1.teacher_id = ? AND EXTRACT(DAY FROM t2.start_lection)=? AND EXTRACT(MONTH FROM t2.start_lection)=?";
    private static final String GET_TIMETEBALE_TEACHER_FOR_UPDATE = "SELECT t1.* FROM lection t1 JOIN lectiontime t2 ON t1.lecture_time_id = t2.id WHERE t1.teacher_id = ? AND EXTRACT(DAY FROM t2.start_lection)=? AND EXTRACT(MONTH FROM t2.start_lection)=? AND t1.id !=?";
    private static final String GET_TIMETEBALE_STUDENT = "SELECT t1.* FROM lection t1 JOIN lectiontime t2 ON t1.lecture_time_id = t2.id JOIN group_lection g ON t1.id = g.lection_id WHERE g.group_id=? AND EXTRACT(DAY FROM t2.start_lection)=? AND EXTRACT(MONTH FROM t2.start_lection)=?";
    private static final String SELECT_BY_AUDIENCE_DATE_LECTURE_TIME_WHEN_CREATE = "SELECT COUNT(*) FROM lection WHERE audience_id=? AND lecture_time_id=?";
    private static final String SELECT_BY_AUDIENCE_DATE_LECTURE_TIME_WHEN_UPDATE = "SELECT COUNT(*) FROM lection WHERE audience_id=? AND lecture_time_id=? AND id != ?";
    private static final String SELECT_BY_TEACHER_ID_AND_PERIOD = "SELECT t1.* FROM lection t1 JOIN lectiontime t2 ON  t1.lecture_time_id = t2.id WHERE (DATE(t2.start_lection) BETWEEN ? AND ?) AND t1.teacher_id = ?";
    private static final String INSERT_GROUP = "INSERT INTO group_lection (group_id, lection_id) VALUES (?, ?)";
    private static final String DELETE_GROUP = "DELETE FROM group_lection WHERE group_id = ? AND lection_id = ?";
    private static final String GET_GROUP_LECTURE = "SELECT group_id FROM group_lection WHERE lection_id=?";
    private final JdbcTemplate jdbcTemplate;
    private LectureMapper lectureMapper;
    private LectureJoinMapper lectureJoinMapper;

    @Autowired
    public JdbcLecture(JdbcTemplate jdbcTemplate, LectureMapper lectureMapper, LectureJoinMapper lectureJoinMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.lectureMapper = lectureMapper;
        this.lectureJoinMapper = lectureJoinMapper;
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
        lecture.getGroup().stream().forEach(group -> addlectionGroup(group.getId(), lecture.getId()));
    }

    @Override
    public void addlectionGroup(Long groupId, Long lectionId) {
        if (Objects.isNull(groupId)) {
            throw new IllegalArgumentException("ID группы не может иметь значение null");
        }
        if (Objects.isNull(lectionId)) {
            throw new IllegalArgumentException("ID лекции не может иметь значение null");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(INSERT_GROUP);
            ps.setLong(1, groupId);
            ps.setLong(2, lectionId);
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Lecture lecture) {
        updateLecture(lecture);
    }

    @Override
    public void update(Lecture lecture, Lecture lectureOld) {
        updateLecture(lecture);
        updateGroup(lectureOld, lecture);
        deleteGroup(lectureOld, lecture);
    }

    @Override
    public void updateLecture(Lecture lecture) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_LECTURE);
            ps.setLong(1, lecture.getCathedra().getId());
            ps.setLong(2, lecture.getTeacher().getId());
            ps.setLong(3, lecture.getSubject().getId());
            ps.setLong(4, lecture.getTime().getId());
            ps.setLong(5, lecture.getAudience().getId());
            ps.setLong(6, lecture.getId());
            return ps;
        });
    }

    private void updateGroup(Lecture lectureOld, Lecture lecturerNew) {
        Predicate<Group> groupPredicate = lectureOld.getGroup()::contains;
        lecturerNew.getGroup().stream().filter(groupPredicate.negate()::test)
                .forEach(group -> jdbcTemplate.update(INSERT_GROUP, group.getId(), lecturerNew.getId()));

    }

    private void deleteGroup(Lecture lectureOld, Lecture lecturerNew) {
        Predicate<Group> groupPredicate = lecturerNew.getGroup()::contains;
        lectureOld.getGroup().stream().filter(groupPredicate.negate()::test)
                .forEach(group -> jdbcTemplate.update(DELETE_GROUP, group.getId(), lecturerNew.getId()));
    }

    @Override
    public List<Long> getListGroupForLecture(Long lectureId) {
        return jdbcTemplate.query(GET_GROUP_LECTURE, new Object[]{lectureId}, (rs, rowNum) -> rs.getLong("group_id"));
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
        return jdbcTemplate.query(FIND_ALL_AT_ONCE, lectureJoinMapper);
    }

    @Override
    public Page<Lecture> findPaginatedLecture(Pageable pageable) {
        int total = jdbcTemplate.queryForObject(COUNT_ALL, Integer.class);
        List<Lecture> audiences = jdbcTemplate.query(SELECT_BY_PAGE, lectureJoinMapper, pageable.getPageSize(), pageable.getOffset());

        return new PageImpl<>(audiences, pageable, total);
    }

    @Override
    public List<Lecture> getTimetableStudent(Student student, LocalDate localDate) {
        return jdbcTemplate.query(GET_TIMETEBALE_STUDENT, ps -> {
            ps.setLong(1, student.getGroup().getId());
            ps.setInt(2, localDate.getDayOfMonth());
            ps.setInt(3, localDate.getMonthValue());
        }, lectureMapper);
    }

    @Override
    public List<Lecture> getTimetableTeacherForCreate(Teacher teacher, LocalDate localDate) {
        return jdbcTemplate.query(GET_TIMETEBALE_TEACHER_FOR_CREATE, ps -> {
            ps.setLong(1, teacher.getId());
            ps.setInt(2, localDate.getDayOfMonth());
            ps.setInt(3, localDate.getMonthValue());
        }, lectureMapper);
    }

    @Override
    public List<Lecture> getTimetableTeacherForUpdate(Teacher teacher, LocalDate localDate, Lecture lecture) {
        return jdbcTemplate.query(GET_TIMETEBALE_TEACHER_FOR_UPDATE, ps -> {
            ps.setLong(1, teacher.getId());
            ps.setInt(2, localDate.getDayOfMonth());
            ps.setInt(3, localDate.getMonthValue());
            ps.setLong(4, lecture.getId());
        }, lectureMapper);
    }

    @Override
    public List<Lecture> findLecturesByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        return jdbcTemplate.query(SELECT_BY_TEACHER_ID_AND_PERIOD, ps -> {
            ps.setObject(1, start);
            ps.setObject(2, end);
            ps.setLong(3, teacher.getId());
        }, lectureMapper);
    }

    @Override
    public boolean isSingle(Lecture lecture) {
        Integer result = jdbcTemplate.queryForObject(FIND_LECTURE, Integer.class, lecture.getTeacher().getId(), lecture.getSubject().getId(), lecture.getTime().getId() ,lecture.getAudience().getId());
        return result != null && result > 0;
    }

    @Override
    public boolean findByAudienceDateAndLectureTimeForCreate(Audience audience, LectureTime time) {
        Integer result = jdbcTemplate.queryForObject(SELECT_BY_AUDIENCE_DATE_LECTURE_TIME_WHEN_CREATE, Integer.class, audience.getId(), time.getId());
        return result != null && result > 0;
    }

    @Override
    public boolean findByAudienceDateAndLectureTimeForUpdate(Audience audience, LectureTime time, Long id) {
        Integer result = jdbcTemplate.queryForObject(SELECT_BY_AUDIENCE_DATE_LECTURE_TIME_WHEN_UPDATE, Integer.class, audience.getId(), time.getId(), id);
        return result != null && result > 0;
    }
}
