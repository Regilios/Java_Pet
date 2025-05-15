package org.example.univer.dao.interfaces;

import org.example.univer.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DaoStudentInterface extends DaoInterfaces<Student> {
     Page<Student> findPaginatedStudents(Pageable pageable);
     boolean isSingle(Student student);
     Integer checkGroupSize(Student student);
}
