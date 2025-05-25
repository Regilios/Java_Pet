package org.example.univer.repositories;

import org.example.univer.models.Lecture;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface LectureRepository extends JpaRepository<Lecture,Long> {
    @Query("SELECT l FROM Lecture l")
    Page<Lecture> findAllLectures(Pageable pageable);

    @Query("SELECT l FROM Lecture l JOIN FETCH l.groups WHERE l.id IN :lectureIds")
    List<Lecture> findWithGroupsByIdIn(@Param("lectureIds") List<Long> lectureIds);
    boolean existsByTeacherIdAndSubjectIdAndTimeIdAndAudienceId (Long teacherId, Long subjectId, Long lectureTimeId, Long audienceId);

    @Query("SELECT DISTINCT l FROM Lecture l LEFT JOIN FETCH l.groups g WHERE l.id = :id")
    Optional<Lecture> findByIdWithGroups(@Param("id") Long id);

    @Query("SELECT l FROM Lecture l JOIN l.time t WHERE l.teacher.id = :teacherId AND t.startLecture BETWEEN :startOfDay AND :endOfDay")
    List<Lecture> getTimetableTeacherForCreate(
            @Param("teacherId") Long id,
            @Param("startOfDay") LocalDateTime startOfDay,
            @Param("endOfDay") LocalDateTime endOfDay);

    @Query("SELECT l FROM Lecture l WHERE l.teacher.id = :teacherId AND l.time.id = :timeId AND l.id <> :lectureId")
    List<Lecture> getTimetableTeacherForUpdate(
            @Param("teacherId") Long id,
            @Param("timeId") Long timeId,
            @Param("lectureId")  Long lectureId);

    @Query("SELECT l FROM Lecture l JOIN l.time t WHERE t.startLecture BETWEEN :startLecture AND :endLecture AND l.teacher.id = :teacherId")
    List<Lecture> findLecturesByTeacherAndPeriod(
            @Param("startLecture") LocalDateTime startLecture,
            @Param("endLecture") LocalDateTime endLecture,
            @Param("teacherId") Long teacherId
    );

    boolean existsByTime_IdAndAudience_Id(Long lectureTimeId, Long audienceId);

    @Query("SELECT COUNT(l) > 0 FROM Lecture l JOIN l.audience a JOIN l.time t WHERE t.id = :timeId AND a.id = :audienceId AND l.id <> :currentLectureId")
    boolean findByAudienceDateAndLectureTimeForUpdate(@Param("timeId") Long timeId,
                                                @Param("audienceId") Long audienceId,
                                                @Param("currentLectureId") Long currentLectureId);
}

