package org.example.univer.dao.interfaces;

import org.example.univer.models.Teacher;

public interface DaoTeacherInterface extends DaoInterfaces<Teacher> {
    void addSubject(Long teacherId, Long subjectId);
    void removeSubject(Long teacherId, Long subjectId);

    boolean isSingle(Teacher teacher);
}
