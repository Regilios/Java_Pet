package org.example.univer.repositories;

import org.example.univer.models.LectureTime;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;

@Repository
public interface LectureTimeRepository extends JpaRepository<LectureTime, Long> {
    boolean existsById(Long id);
    @Query("SELECT COUNT(lt) FROM LectureTime lt WHERE lt.startLecture = :startLecture AND lt.endLecture = :endLecture")
    Long countByStartLectureAndEndLecture(@Param("startLecture") LocalDateTime startLecture,
                                          @Param("endLecture") LocalDateTime endLecture);
}
