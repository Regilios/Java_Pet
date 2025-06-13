package org.example.univer.repositories;

import org.example.univer.models.Teacher;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Long> {
    boolean existsByFirstNameAndLastName(String firstName, String lastName);
    boolean existsById(Long id);
    @EntityGraph(attributePaths = {"cathedra", "subjects"})
    List<Teacher> findAll();
    @EntityGraph(attributePaths = {"cathedra", "subjects", "vacation"})
    Optional<Teacher> findById(Long id);

    // Загрузка всех учителей с их subjects
    @Query("""
    SELECT DISTINCT t FROM Teacher t
    LEFT JOIN FETCH t.vacation
    WHERE t IN :teachers
""")
    List<Teacher> findAllWithVacation(@Param("teachers") List<Teacher> teachers);
}
