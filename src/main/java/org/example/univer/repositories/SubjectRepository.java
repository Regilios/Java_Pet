package org.example.univer.repositories;

import org.example.univer.models.Subject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SubjectRepository extends JpaRepository<Subject, Long> {
    boolean existsByName(String name);

    boolean existsByTeachers_IdAndId(Long teacherId, Long subjectId);

    List<Subject> findByTeachers_Id(Long teacherId);
}
