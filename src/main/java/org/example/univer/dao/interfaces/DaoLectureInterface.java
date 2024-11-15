package org.example.univer.dao.interfaces;

import org.example.univer.models.Lecture;
import org.example.univer.models.Student;
import org.example.univer.models.Teacher;

import java.time.LocalDate;
import java.util.List;

public interface DaoLectureInterface extends DaoInterfaces<Lecture> {
    List<Lecture> getTimetable(Student entity, LocalDate localDate);
    List<Lecture> getTimetable(Teacher entity, LocalDate localDate);
}
