package org.example.univer.dao.interfaces;

import org.example.univer.dao.models.Teacher;

public interface DaoTeacherInterfaces extends DaoInterfaces<Teacher> {
    void teacherAppointSubject(Long teacher_id, Long subject_id);
    void deleteTeacherToSubject(Long teacher_id, Long subject_id);
}
