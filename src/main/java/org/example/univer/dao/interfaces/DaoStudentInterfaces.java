package org.example.univer.dao.interfaces;

import org.example.univer.dao.models.Group;
import org.example.univer.dao.models.Student;

import java.util.List;

public interface DaoStudentInterfaces extends DaoInterfaces<Student> {
     List<Student> findAllStudentByIdGroup(Group group);
}
