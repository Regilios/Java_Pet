package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcCathedra;
import org.example.univer.dao.jdbc.JdbcGroup;
import org.example.univer.models.Group;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("jdbc")
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
        group.setName("test");
        group.setCathedra(jdbcCathedra.findById(1L));
        jdbcGroup.create(group);
        Group checkGroup = jdbcGroup.findById(group.getId());

        assertEquals(group, checkGroup);
        assertEquals(group.getId(), checkGroup.getId());
        assertEquals(group.getName(), checkGroup.getName());
        assertEquals(group.getCathedra(), checkGroup.getCathedra());
    }

    @Test
    void checkUpdateGroup() {
        Group group = jdbcGroup.findById(1L);
        group.setName("test");
        jdbcGroup.update(group);

        assertEquals("test", jdbcGroup.findById(1L).getName());
    }

    @Test
    void checkFindByIdGroup() {
        Group group = new Group();
        group.setName("test");
        group.setCathedra(jdbcCathedra.findById(1L));
        jdbcGroup.create(group);

        assertThat(group, is(notNullValue()));
        assertThat(group.getId(), is(equalTo(group.getId())));
        assertEquals(jdbcGroup.findById(group.getId()), group);
    }

    @Test
    void checkDeletedGroup() {
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

    @Test
    void checkIsSingleGroup() {
        Group group = new Group();
        group.setName("test");
        group.setCathedra(jdbcCathedra.findById(1L));

        assertFalse(jdbcGroup.isSingle(group));
    }
}
