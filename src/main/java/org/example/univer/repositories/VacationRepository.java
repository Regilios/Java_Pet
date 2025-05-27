package org.example.univer.repositories;

import org.example.univer.models.Vacation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
@Repository
public interface VacationRepository extends JpaRepository<Vacation, Long> {
    boolean existsByStartJobAndEndJobAndTeacher_Id(LocalDate startJob,LocalDate endJob,Long teacher_id);

    @EntityGraph(attributePaths = {"teacher", "teacher.cathedra", "teacher.subjects"})
    @Query("SELECT v FROM Vacation v WHERE v.id = :id")
    Optional<Vacation> findByIdWithTeacherCathedraAndSubjects(@Param("id") Long id);

    @EntityGraph(attributePaths = {"teacher", "teacher.cathedra", "teacher.subjects"})
    List<Vacation> findByTeacher_Id(Long teacherId);

    @Modifying
    @Transactional
    @Query("DELETE FROM Vacation v WHERE v.id = :id")
    void deleteVacationById(@Param("id") Long id);
}
