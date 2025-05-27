package org.example.univer.repositories;

import org.example.univer.models.Group;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface GroupRepository extends JpaRepository<Group, Long> {
    boolean existsByName(String name);

    @EntityGraph(attributePaths = {"cathedra"})
    Optional<Group> findById(Long groupId);

    @EntityGraph(attributePaths = {"cathedra"})
    List<Group> findAll();
}
