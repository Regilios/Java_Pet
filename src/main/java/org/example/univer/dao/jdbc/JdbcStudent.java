package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoStudentInterface;
import org.example.univer.dao.mapper.StudentMapper;
import org.example.univer.models.Group;
import org.example.univer.models.Student;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;

@Component
public class JdbcStudent implements DaoStudentInterface {
    private static final String CREATE_STUDENT = "INSERT INTO students (firstName, lastName, gender, address, email, phone, birthday, group_id) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
    private static final String UPDATE_STUDENT = "UPDATE students SET firstName=?, lastName=?, gender=?, address=?, email=?, phone=?, birthday=?, group_id=? WHERE id = ?";
    private static final String DELETE_STUDENT = "DELETE FROM students WHERE id=?";
    private static final String SELECT_BY_PAGE = "SELECT s.*, g.name AS group_name FROM students s JOIN groups g ON s.group_id = g.id LIMIT ? OFFSET ?";
    private static final String COUNT_ALL = "SELECT COUNT(*) FROM students";
    private static final String GET_BY_ID = "SELECT s.*, g.name AS group_name FROM students s JOIN groups g ON s.group_id = g.id WHERE s.id=?";
    private static final String FIND_STUDENT_BY_GROUP_ID = "SELECT s.*, g.name AS group_name FROM students s JOIN groups g ON s.group_id = g.id WHERE g.id=? ORDER BY id";
    private static final String FIND_HOW_MANY_STUDENT_IN_GROUP = "SELECT COUNT(*) FROM students WHERE group_id=?";
    private static final String FIND_STUDENT = "SELECT COUNT(*) FROM students WHERE firstname=? AND lastname=? AND address=? AND email=? AND birthday=? AND group_id=?";
    private static final String FIND_ALL = "SELECT s.*, g.name AS group_name FROM students s JOIN groups g ON s.group_id = g.id ORDER BY id";

    private final JdbcTemplate jdbcTemplate;
    private StudentMapper studentMapper;

    @Autowired
    public JdbcStudent(JdbcTemplate jdbcTemplate, StudentMapper studentMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.studentMapper = studentMapper;
    }

    @Override
    public void create(Student student) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(CREATE_STUDENT, new String[]{"id"});
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getGender().toString());
            ps.setString(4, student.getAddress());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getPhone());
            ps.setObject(7, student.getBirthday());
            ps.setLong(8, student.getGroup().getId());

            return ps;
        }, keyHolder);
        student.setId((long) keyHolder.getKeyList().get(0).get("id"));
    }

    @Override
    public void update(Student student) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_STUDENT);
            ps.setString(1, student.getFirstName());
            ps.setString(2, student.getLastName());
            ps.setString(3, student.getGender().toString());
            ps.setString(4, student.getAddress());
            ps.setString(5, student.getEmail());
            ps.setString(6, student.getPhone());
            ps.setObject(7, student.getBirthday());
            ps.setLong(8, student.getGroup().getId());
            ps.setLong(9, student.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_STUDENT, id);
    }

    @Override
    public Student findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, studentMapper, id);
    }

    @Override
    public List<Student> findAll() {
        return jdbcTemplate.query(FIND_ALL, studentMapper);
    }

    @Override
    public Page<Student> findPaginatedStudents(Pageable pageable) {
        int total = jdbcTemplate.queryForObject(COUNT_ALL, Integer.class);
        List<Student> students = jdbcTemplate.query(SELECT_BY_PAGE, studentMapper, pageable.getPageSize(), pageable.getOffset());

        return new PageImpl<>(students, pageable, total);
    }

    public List<Student> findAllStudentByGroupId(Group group) {
        Long id = group.getId();
        return jdbcTemplate.query(FIND_STUDENT_BY_GROUP_ID, studentMapper, id);
    }

    @Override
    public boolean isSingle(Student student) {
        Integer result = jdbcTemplate.queryForObject(FIND_STUDENT, Integer.class, student.getFirstName(), student.getLastName(), student.getAddress(), student.getEmail(), student.getBirthday(), student.getGroup().getId());
        return result != null && result > 0;
    }

    @Override
    public Integer checkGroupSize(Student student) {
        Integer result = jdbcTemplate.queryForObject(FIND_HOW_MANY_STUDENT_IN_GROUP, Integer.class, student.getGroup().getId());
        return result;
    }
}
