package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoGroupInterfaces;
import org.example.univer.dao.mapper.GroupMapper;
import org.example.univer.dao.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;

@Component
public class JdbcGroup implements DaoGroupInterfaces {
    private static final String SQL_FIND_ALL = "SELECT * FROM groups ORDER BY id";

    private static final String SQL_GET_BY_ID = "SELECT * FROM groups WHERE id = ?";
    private static final String SQL_CREATE = "INSERT INTO groups (name, cathedra_id) VALUES (?, ?)";
    private static final String SQL_GROUP_APPOINT_LECTION = "INSERT INTO group_lection (group_id, lection_id) VALUES (?, ?)";
    private static final String SQL_DELETE = "DELETE FROM groups WHERE id = ?";
    private static final String SQL_DELETE_GROUP_TO_LECTION = "DELETE FROM group_lection WHERE group_id = ? AND lection_id=?";
    private static final String SQL_UPDATE = "UPDATE groups SET name=?, cathedra_id=? WHERE id=?";
    private final JdbcTemplate jdbcTemplate;
    private GroupMapper groupMapper;

    @Autowired
    public JdbcGroup(JdbcTemplate jdbcTemplate, GroupMapper groupMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.groupMapper = groupMapper;
    }

    @Override
    public void create(Group group) {
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_CREATE, new String[]{"id"});
            ps.setString(1, group.getName());
            ps.setLong(2, group.getCathedra().getId());
            return ps;
        }, keyHolder);

//        Устанавливаем ключ обрновления
//        int id =  jdbcTemplate.update(SQL_CREATE, group.getName(), group.getCathedra().getId());
    }

    public void groupAppointLection(Long group_id, Long lection_id) {
        if (Objects.isNull(group_id)) {
            throw new IllegalArgumentException("Группа с таким id не существует или не найден!");
        }
        if (Objects.isNull(lection_id)) {
            throw new IllegalArgumentException("Лекция с таким id не существует или не найден!");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_GROUP_APPOINT_LECTION);
            ps.setLong(1, group_id);
            ps.setLong(2, lection_id);
            return ps;
        }, keyHolder);
    }

    @Override
    public void update(Group group) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(SQL_UPDATE);
            ps.setString(1, group.getName());
            ps.setLong(2, group.getCathedra().getId());
            ps.setLong(3, group.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(SQL_DELETE, id);
    }

    public void deleteGroupToLEction(Long group_id, Long lection_id) {
        jdbcTemplate.update(SQL_DELETE_GROUP_TO_LECTION, group_id, lection_id);
    }

    @Override
    public Group findById(Long id) {
        return jdbcTemplate.queryForObject(SQL_GET_BY_ID, groupMapper, id);
    }

    @Override
    public List<Group> findAll() {
        return jdbcTemplate.query(SQL_FIND_ALL, groupMapper);
    }
}
