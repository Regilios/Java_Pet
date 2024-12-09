package org.example.univer.dao.interfaces;

import org.example.univer.models.*;

import java.time.LocalDate;
import java.util.List;

public interface DaoLectureInterface extends DaoInterfaces<Lecture> {
    List<Lecture> getTimetableStudent(Student entity, LocalDate localDate);
    List<Lecture> getTimetableTeacher(Teacher entity, LocalDate localDate);

    boolean isSingle(Lecture lecture);
    boolean findByAudienceDateAndLectureTime(Audience audience, LectureTime time);
}
