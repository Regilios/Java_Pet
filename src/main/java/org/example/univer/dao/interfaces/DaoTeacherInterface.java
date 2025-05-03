package org.example.univer.dao.interfaces;

import org.example.univer.models.Teacher;

public interface DaoTeacherInterface extends DaoInterfaces<Teacher> {
    boolean isSingle(Teacher teacher);
}
