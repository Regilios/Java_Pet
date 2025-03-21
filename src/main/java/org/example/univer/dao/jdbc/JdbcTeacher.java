package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoTeacherInterface;
import org.example.univer.dao.mapper.TeacherMapper;
import org.example.univer.exeption.TeacherExeption;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.function.Predicate;

@Component
public class JdbcTeacher implements DaoTeacherInterface {
    private static final String CREATE_TEACHER = "INSERT INTO teacher (firstName, lastName, gender, address, email, phone, birthday, cathedra_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_TEACHER = "UPDATE teacher SET firstName=?, lastName=?, gender=?, address=?, email=?, phone=?, birthday=?, cathedra_id=? WHERE id = ?";
    private static final String DELETE_TEACHER = "DELETE FROM teacher WHERE id=?";
    private static final String GET_BY_ID = "SELECT t.*, c.name AS cathedra_name FROM teacher t JOIN cathedra c ON t.cathedra_id = c.id WHERE t.id=?";
    private static final String FIND_TEACHER = "SELECT COUNT(*) FROM teacher WHERE  firstName=? AND lastName=? AND gender=? AND birthday=?";
    private static final String ADD_SUBJECT_TEACHER = "INSERT INTO teacher_subject (teacher_id, subject_id) VALUES (?, ?)";
    private static final String REMOVE_SUBJECT_TEACHER = "DELETE FROM teacher_subject WHERE teacher_id=? AND subject_id=?";
    private static final String GET_SUBJECT_TEACHER = "SELECT subject_id FROM teacher_subject WHERE teacher_id=?";
    private static final String FIND_ALL = "SELECT t.*, c.name AS cathedra_name FROM teacher t JOIN cathedra c ON t.cathedra_id = c.id ORDER BY id";
    private final JdbcTemplate jdbcTemplate;
    private TeacherMapper teacherMapper;


    @Autowired
    public JdbcTeacher(JdbcTemplate jdbcTemplate, TeacherMapper teacherMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.teacherMapper = teacherMapper;
    }

    @Override
    public void create(Teacher teacher) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_TEACHER, new String[]{"id"});
            ps.setString(1, teacher.getFirstName());
            ps.setString(2, teacher.getLastName());
            ps.setObject(3, teacher.getGender().toString());
            ps.setString(4, teacher.getAddress());
            ps.setString(5, teacher.getEmail());
            ps.setString(6, teacher.getPhone());
            ps.setObject(7, teacher.getBirthday());
            ps.setLong(8, teacher.getCathedra().getId());
            return ps;
        }, keyHolder);
        teacher.setId((long) keyHolder.getKeyList().get(0).get("id"));
        teacher.getSubject().stream().forEach(subject -> addSubject(teacher.getId(), subject.getId()));
    }

    public void addSubject(Long teacherId, Long subjectId) {
        if (Objects.isNull(teacherId)) {
            throw new TeacherExeption("ID учителя не может иметь значение null");
        }
        if (Objects.isNull(subjectId)) {
            throw new TeacherExeption("ID предмета не может иметь значение null");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(ADD_SUBJECT_TEACHER);
            ps.setLong(1, teacherId);
            ps.setLong(2, subjectId);
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Teacher teacher) {
        updateTeacher(teacher);
    }

    @Override
    public void update(Teacher teacher, Teacher teacherOld) {
        updateTeacher(teacher);
        updateSubjects(teacherOld, teacher);
        deleteSubjects(teacherOld, teacher);
    }

    private void updateTeacher(Teacher teacher) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_TEACHER);
            ps.setString(1, teacher.getFirstName());
            ps.setString(2, teacher.getLastName());
            ps.setObject(3, teacher.getGender().toString());
            ps.setString(4, teacher.getAddress());
            ps.setString(5, teacher.getEmail());
            ps.setString(6, teacher.getPhone());
            ps.setObject(7, teacher.getBirthday());
            ps.setLong(8, teacher.getCathedra().getId());
            ps.setLong(9, teacher.getId());
            return ps;
        });
    }

    private void updateSubjects(Teacher teacherOld, Teacher teacherNew) {
        Predicate<Subject> subjPredicate = teacherOld.getSubject()::contains;
        teacherNew.getSubject().stream().filter(subjPredicate.negate()::test)
                .forEach(subject -> jdbcTemplate.update(ADD_SUBJECT_TEACHER, teacherNew.getId(),subject.getId()));

    }
    private void deleteSubjects(Teacher teacherOld, Teacher teacherNew) {
        Predicate<Subject> subjPredicate = teacherNew.getSubject()::contains;
        teacherOld.getSubject().stream().filter(subjPredicate.negate()::test)
                .forEach(subject -> jdbcTemplate.update(REMOVE_SUBJECT_TEACHER, teacherNew.getId(), subject.getId()));
    }


    public void removeSubject(Long teacherId, Long subjectId) {
        jdbcTemplate.update(REMOVE_SUBJECT_TEACHER, teacherId, subjectId);
    }

    public List<Long> getListSubjectForTeacher(Long teacherId) {
        return jdbcTemplate.query(GET_SUBJECT_TEACHER, new Object[]{teacherId}, (rs, rowNum) -> rs.getLong("subject_id"));
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_TEACHER, id);
    }

    @Override
    public Teacher findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, teacherMapper, id);
    }

    @Override
    public List<Teacher> findAll() {
        return jdbcTemplate.query(FIND_ALL, teacherMapper);
    }

    @Override
    public boolean isSingle(Teacher teacher) {
        Integer result = jdbcTemplate.queryForObject(FIND_TEACHER, Integer.class, teacher.getFirstName(), teacher.getLastName(), teacher.getGender().toString(), teacher.getBirthday());
        return result != null && result > 0;
    }
}
