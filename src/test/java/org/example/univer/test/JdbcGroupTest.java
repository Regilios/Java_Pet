package org.example.univer.test;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcCathedra;
import org.example.univer.dao.jdbc.JdbcGroup;
import org.example.univer.dao.models.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class JdbcGroupTest {

    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcGroup jdbcGroup;
    @Autowired
    private JdbcCathedra jdbcCathedra;
    private final static String TABLE_NAME = "groups";

    @Test
    void checkCreatedGroup() {
        Group group = new Group();
        group.setId(3L);
        group.setNameGroup("test");
        group.setCathedra(jdbcCathedra.findById(1L));
        jdbcGroup.create(group);
        Group checkGroup = jdbcGroup.findById(3L);

        assertEquals(group, checkGroup);
        assertEquals(group.getId(), checkGroup.getId());
        assertEquals(group.getName(), checkGroup.getName());
        assertEquals(group.getCathedra(), checkGroup.getCathedra());
    }

    @Test
    void checkUpdateGroup() {
        Group group = jdbcGroup.findById(1L);
        group.setNameGroup("test");

        assertEquals("test", group.getName());
    }

    @Test
    void checkFindByIdGroup() {
        Group group = jdbcGroup.findById(1L);

        assertEquals("Этонсель", group.getName());
    }

    @Test
    void checkDeletedGroup() {
//        Group group = new Group();
//        group.setId(3L);
//        group.setNameGroup("test");
//        group.setCathedra(jdbcCathedra.findById(1L).getCathedra());
//        jdbcGroup.create(group);
//        jdbcGroup.deleteById(3L);
//        Boolean present = Optional.of(jdbcGroup.findById(3L)).isPresent();
//        Assertions.assertFalse(present);

        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcGroup.deleteById(2L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllGroups() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcGroup.findAll().size();

        assertEquals(expected, actual);
    }
}
