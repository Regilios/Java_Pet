package org.example.univer.test.service;

import org.example.univer.config.TestSpringConfig;
import org.example.univer.models.Cathedra;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.annotation.DirtiesContext;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.transaction.annotation.Transactional;

import static junit.framework.Assert.assertEquals;

@ExtendWith(SpringExtension.class)
@ContextConfiguration(classes = TestSpringConfig.class)
@DirtiesContext(classMode = DirtiesContext.ClassMode.AFTER_EACH_TEST_METHOD)
@Transactional
public class CathedraServiceTest {

    @Autowired
    private org.example.univer.services.CathedraService cathedraService;

    @Test
    void checkCreatedTeacher() {
        Cathedra cathedra1 = cathedraService.findById(1L);
        cathedra1.setName("test");
        cathedraService.update(cathedra1);

        assertEquals("test", cathedraService.findById(1L).getName());
    }
}
