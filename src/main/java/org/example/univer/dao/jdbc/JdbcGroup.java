package org.example.univer.dao.jdbc;

import org.example.univer.dao.interfaces.DaoGroupInterface;
import org.example.univer.dao.mapper.GroupMapper;
import org.example.univer.models.Group;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.PreparedStatement;
import java.util.List;
import java.util.Objects;
import java.util.stream.Collectors;

@Component
public class JdbcGroup implements DaoGroupInterface {
    private static final String FIND_ALL = "SELECT g.*, c.name AS cathedra_name FROM groups g JOIN cathedra c ON g.cathedra_id = c.id ORDER BY id";
    private static final String FIND_GROUP = "SELECT COUNT(*) FROM groups WHERE name=?";
    private static final String GET_BY_ID = "SELECT g.*, c.name AS cathedra_name FROM groups g JOIN cathedra c ON g.cathedra_id = c.id WHERE g.id=?";
    private static final String CREATE_GROUP = "INSERT INTO groups (name, cathedra_id) VALUES (?, ?)";
    private static final String GROUP_ADD_LECTION = "INSERT INTO group_lection (group_id, lection_id) VALUES (?, ?)";
    private static final String DELETE_GROUP= "DELETE FROM groups WHERE id=?";
    private static final String GROUP_DELETE_LECTION = "DELETE FROM group_lection WHERE group_id=? AND lection_id=?";
    private static final String UPDATE_GROUP = "UPDATE groups SET name=?, cathedra_id=? WHERE id=?";

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
            PreparedStatement ps = connection.prepareStatement(CREATE_GROUP, new String[]{"id"});
            ps.setString(1, group.getName());
            ps.setLong(2, group.getCathedra().getId());
            return ps;
        }, keyHolder);
        group.setId((long) keyHolder.getKeyList().get(0).get("id"));
    }

    @Override
    public void addlection(Long groupId, Long lectionId) {
        if (Objects.isNull(groupId)) {
            throw new IllegalArgumentException("ID группы не может иметь значение null");
        }
        if (Objects.isNull(lectionId)) {
            throw new IllegalArgumentException("ID лекции не может иметь значение null");
        }
        KeyHolder keyHolder = new GeneratedKeyHolder();
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(GROUP_ADD_LECTION);
            ps.setLong(1, groupId);
            ps.setLong(2, lectionId);
            return ps;
        }, keyHolder);
    }

    @Override
    public void removeLection(Long groupId, Long lectionId) {
        jdbcTemplate.update(GROUP_DELETE_LECTION, groupId, lectionId);
    }

    @Override
    public void update(Group group) {
        jdbcTemplate.update(connection -> {
            PreparedStatement ps = connection.prepareStatement(UPDATE_GROUP);
            ps.setString(1, group.getName());
            ps.setLong(2, group.getCathedra().getId());
            ps.setLong(3, group.getId());
            return ps;
        });
    }

    @Override
    public void deleteById(Long id) {
        jdbcTemplate.update(DELETE_GROUP, id);
    }

    @Override
    public Group findById(Long id) {
        return jdbcTemplate.queryForObject(GET_BY_ID, groupMapper, id);
    }

    @Override
    public List<Group> getGroupById(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            throw new IllegalArgumentException("The list of groups IDs cannot be null or empty");
        }

        String ids = groupIds.stream().map(String::valueOf).collect(Collectors.joining(", "));
        String FIND_ALL_GROUP_BY_ID = "SELECT g.*, c.name AS cathedra_name FROM groups g JOIN cathedra c ON g.cathedra_id = c.id WHERE g.id IN ("+ ids +")";

        return jdbcTemplate.query(FIND_ALL_GROUP_BY_ID, groupMapper);
    }
    @Override
    public List<Group> findAll() {
        return jdbcTemplate.query(FIND_ALL, groupMapper);
    }

    @Override
    public boolean isSingle(Group group) {
        Integer result = jdbcTemplate.queryForObject(FIND_GROUP, Integer.class, group.getName());
        return result != null && result > 0;
    }
}
