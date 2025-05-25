package org.example.univer.repositories;

import org.example.univer.models.Group;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
public class GroupRepositoryTest {
    @Autowired
    private GroupRepository groupRepository;

    @Test
    void whenSaveGroup_thenGroupIsPersisted() {
        Group group = new Group();
        group.setName("TestGroup");

        Group saved = groupRepository.save(group);

        assertNotNull(saved.getId());
        assertEquals("TestGroup", saved.getName());
    }

    @Test
    void whenDeleteGroup_thenGroupIsRemoved() {
        Group group = new Group();
        group.setName("GroupToDelete");
        groupRepository.save(group);

        groupRepository.deleteById(group.getId());

        Optional<Group> found = groupRepository.findById(group.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void whenExistsByName_thenReturnTrue() {
        Group group = new Group();
        group.setName("UniqueName");
        groupRepository.save(group);

        boolean exists = groupRepository.existsByName("UniqueName");

        assertTrue(exists);
    }

    @Test
    void whenFindAll_thenReturnAllGroupsWithCathedra() {
        Group g1 = new Group();
        g1.setName("Group1");
        Group g2 = new Group();
        g2.setName("Group2");

        groupRepository.saveAll(List.of(g1, g2));

        List<Group> groups = groupRepository.findAll();

        assertTrue(groups.size() >= 2);
        assertTrue(groups.stream().anyMatch(g -> g.getName().equals("Group1")));
        assertTrue(groups.stream().anyMatch(g -> g.getName().equals("Group2")));
    }
}
