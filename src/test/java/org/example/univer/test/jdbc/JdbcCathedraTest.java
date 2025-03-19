package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcCathedra;
import org.example.univer.models.Cathedra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@ActiveProfiles("jdbc")
public class JdbcCathedraTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcCathedra jdbcCathedra;
    private final static String TABLE_NAME = "cathedra";

    @Test
    void checkCreatedCathedra() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");

        jdbcCathedra.create(cathedra);
        Cathedra cathedra1 = jdbcCathedra.findById(cathedra.getId());

        assertEquals(cathedra, cathedra1);
        assertEquals(cathedra.getId(), cathedra1.getId());
        assertEquals(cathedra.getName(), cathedra1.getName());
    }

    @Test
    void checkUpdateCathedra() {
        Cathedra cathedra = jdbcCathedra.findById(1L);
        cathedra.setName("test");
        jdbcCathedra.update(cathedra);

        assertEquals("test", jdbcCathedra.findById(1L).getName());
    }

    @Test
    void checkFindByIdCathedra() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");
        jdbcCathedra.create(cathedra);

        assertEquals(jdbcCathedra.findById(cathedra.getId()), cathedra);
    }

    @Test
    void checkDeletedCathedra() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcCathedra.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllCathedra() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcCathedra.findAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void checkIsSingleCathedra() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("test");

        assertFalse(jdbcCathedra.isSingle(cathedra));
    }
}
