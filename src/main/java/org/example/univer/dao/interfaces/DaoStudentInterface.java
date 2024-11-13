package org.example.univer.dao.interfaces;

import org.example.univer.models.Group;
import org.example.univer.models.Student;

import java.util.List;

public interface DaoStudentInterface extends DaoInterfaces<Student> {
     List<Student> findAllStudentbyGroupId(Group group);
}
