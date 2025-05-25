package org.example.univer.repositories;

import org.example.univer.models.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);

    @EntityGraph(attributePaths = {"cathedra", "subjects"})
    List<Teacher> findAll();

    @EntityGraph(attributePaths = {"cathedra", "subjects"})
    Optional<Teacher> findById(Long id);
}
