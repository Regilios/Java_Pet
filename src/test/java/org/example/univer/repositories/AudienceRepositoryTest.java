package org.example.univer.repositories;

import org.example.univer.dao.repositories.AudienceRepository;
import org.example.univer.models.Audience;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class AudienceRepositoryTest {
    @Autowired
    private AudienceRepository audienceRepository;


    @Test
    void whenSaveAudience_thenAudienceIsPersisted() {
        Audience audience = new Audience();
        audience.setRoomNumber(101);
        audience.setCapacity(50);

        Audience saved = audienceRepository.save(audience);

        assertNotNull(saved.getId());
        assertEquals(101, saved.getRoomNumber());
    }

    @Test
    void whenFindById_thenAudienceIsReturned() {
        Audience audience = new Audience(null, 102, 60);
        audienceRepository.save(audience);

        Optional<Audience> found = audienceRepository.findById(audience.getId());

        assertTrue(found.isPresent());
        assertEquals(102, found.get().getRoomNumber());
    }

    @Test
    void whenDeleteAudience_thenAudienceIsRemoved() {
        Audience audience = new Audience(null, 103, 70);
        audienceRepository.save(audience);

        audienceRepository.deleteById(audience.getId());

        Optional<Audience> found = audienceRepository.findById(audience.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void whenExistsByRoomNumber_thenReturnTrue() {
        Audience audience = new Audience(null, 104, 80);
        audienceRepository.save(audience);

        boolean exists = audienceRepository.existsByRoomNumber(104);
        assertTrue(exists);
    }

    @Test
    void whenFindAllPaginated_thenPageIsReturned() {
        Audience a1 = new Audience(null, 201, 30);
        Audience a2 = new Audience(null, 202, 40);
        audienceRepository.saveAll(List.of(a1, a2));

        Pageable pageable = PageRequest.of(0, 10);
        Page<Audience> page = audienceRepository.findAllByOrderById(pageable);

        assertEquals(2, page.getTotalElements());
        assertEquals(2, page.getContent().size());
    }
}
