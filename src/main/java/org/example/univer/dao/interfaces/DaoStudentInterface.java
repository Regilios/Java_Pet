package org.example.univer.dao.interfaces;

import org.example.univer.models.Group;
import org.example.univer.models.Student;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface DaoStudentInterface extends DaoInterfaces<Student> {
     Page<Student> findPaginatedStudents(Pageable pageable);

     List<Student> findAllStudentByGroupId(Group group);

     boolean isSingle(Student student);

     Integer checkGroupSize(Student student);
}
