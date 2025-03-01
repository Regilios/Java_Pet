package org.example.univer.dao.interfaces;

import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;

import java.util.List;

public interface DaoSubjectInterface extends DaoInterfaces<Subject> {
    boolean checkTeacherAssignedSubject(Teacher teacher, Subject subject);
    boolean isSingle(Subject subject);
    public List<Subject> getSubjectById(Long teacher_id);
}
