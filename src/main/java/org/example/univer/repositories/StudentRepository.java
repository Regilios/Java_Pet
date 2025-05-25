package org.example.univer.repositories;

import org.example.univer.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    @EntityGraph(attributePaths = {"group", "group.cathedra"})
    Page<Student> findAllByOrderById(Pageable pageable);

    @EntityGraph(attributePaths = {"group", "group.cathedra"})
    Optional<Student> findById(Long id);
    int countByGroup_Id(Long groupId);
}
