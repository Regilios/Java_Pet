package org.example.univer.dao.interfaces;

import org.example.univer.models.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;

public interface DaoLectureInterface extends DaoInterfaces<Lecture> {
    Page<Lecture> findPaginatedLecture(Pageable pageable);


    List<Lecture> getTimetableTeacherForCreate(Teacher entity, LocalDate localDate);

    List<Lecture> getTimetableTeacherForUpdate(Teacher teacher, LocalDate localDate, Lecture lecture);

    List<Lecture> findLecturesByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end);

    boolean isSingle(Lecture lecture);

    boolean findByAudienceDateAndLectureTimeForCreate(Audience audience, LectureTime time);

    boolean findByAudienceDateAndLectureTimeForUpdate(Audience audience, LectureTime time, Long id);
}
