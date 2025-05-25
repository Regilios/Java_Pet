package org.example.univer.repositories;

import org.example.univer.models.Cathedra;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class CathedraRepositoryTest {
    @Autowired
    private CathedraRepository cathedraRepository;

    @Test
    void whenSaveCathedra_thenCathedraIsPersisted() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Test");

        Cathedra saved = cathedraRepository.save(cathedra);

        assertNotNull(saved.getId());
        assertEquals("Test", saved.getName());
    }

    @Test
    void whenDeleteCathedra_thenCathedraIsRemoved() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Test");

        cathedraRepository.save(cathedra);
        cathedraRepository.deleteById(cathedra.getId());

        Optional<Cathedra> found = cathedraRepository.findById(cathedra.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void whenExistsByName_thenReturnTrue() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("UniqueName");
        cathedraRepository.save(cathedra);

        boolean exists = cathedraRepository.existsByName("UniqueName");

        assertTrue(exists);
    }

    @Test
    void whenFindAll_thenReturnAllCathedrals() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("UniqueName");
        Cathedra cathedra2 = new Cathedra();
        cathedra2.setName("UniqueName 2");

        cathedraRepository.saveAll(List.of(cathedra, cathedra2));

        List<Cathedra> cathedrals = cathedraRepository.findAll();

        assertTrue(cathedrals.size() >= 2);
        assertTrue(cathedrals.stream().anyMatch(g -> g.getName().equals("UniqueName")));
        assertTrue(cathedrals.stream().anyMatch(g -> g.getName().equals("UniqueName 2")));
    }
}
