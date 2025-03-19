package org.example.univer.dao.interfaces;

import org.example.univer.models.Teacher;

import java.util.List;

public interface DaoTeacherInterface extends DaoInterfaces<Teacher> {
    void addSubject(Long teacherId, Long subjectId);

    void update(Teacher teacher, Teacher teacherOld);

    void removeSubject(Long teacherId, Long subjectId);

    boolean isSingle(Teacher teacher);
    public List<Long> getListSubjectForTeacher(Long teacherId);
}
