package org.example.univer.repositories;

import org.example.univer.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
@Repository
public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByName(String name);

    boolean existsByTeachers_IdAndId(Long teacherId, Long subjectId);

    List<Subject> findByTeachers_Id(Long teacherId);
}
