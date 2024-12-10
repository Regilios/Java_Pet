package org.example.univer.test.jdbc;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.dao.jdbc.JdbcAudience;
import org.example.univer.models.Audience;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;
import static junit.framework.Assert.assertFalse;
import static org.springframework.test.jdbc.JdbcTestUtils.countRowsInTable;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class JdbcAudienceTest {
    @Autowired
    private JdbcTemplate template;
    @Autowired
    private JdbcAudience jdbcAudience;
    private final static String TABLE_NAME = "audience";

    @Test
    void checkCreatedAudience() {
        Audience audience = new Audience();
        audience.setId(7L);
        audience.setRoom(1);
        audience.setCapacity(100);
        jdbcAudience.create(audience);

        Audience audience1 = jdbcAudience.findById(7L);

        assertEquals(audience, audience1);
        assertEquals(audience.getId(), audience1.getId());
        assertEquals(audience.getRoom(), audience1.getRoom());
        assertEquals(audience.getCapacity(), audience1.getCapacity());
    }

    @Test
    void checkUpdateAudience() {
        Audience audience = jdbcAudience.findById(1L);
        audience.setRoom(402);
        jdbcAudience.update(audience);

        assertEquals("402", jdbcAudience.findById(1L).getRoomString());
    }

    @Test
    void checkFindByIdAudience() {
        Audience audience = new Audience();
        audience.setId(7L);
        audience.setRoom(1);
        audience.setCapacity(100);
        jdbcAudience.create(audience);

        assertEquals(jdbcAudience.findById(7L), audience);
    }

    @Test
    void checkDeletedAudience() {
        int expected = countRowsInTable(template, TABLE_NAME) - 1;
        jdbcAudience.deleteById(1L);

        assertEquals(expected, countRowsInTable(template, TABLE_NAME));
    }

    @Test
    void checkFindAllAudience() {
        int expected = countRowsInTable(template, TABLE_NAME);
        int actual = jdbcAudience.findAll().size();

        assertEquals(expected, actual);
    }

    @Test
    void checkIsSingleAudience() {
        Audience audience = new Audience();
        audience.setId(7L);
        audience.setRoom(1);
        audience.setCapacity(100);

        assertFalse(jdbcAudience.isSingle(audience));
    }
}
