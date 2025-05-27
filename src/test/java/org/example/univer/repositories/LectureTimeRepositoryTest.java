package org.example.univer.repositories;

import org.example.univer.models.LectureTime;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
@DataJpaTest
@ActiveProfiles("test")
public class LectureTimeRepositoryTest {

    @Autowired
    private LectureTimeRepository lectureTimeRepository;

    private final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void whenSaveLectureTime_thenPersisted() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2025-05-24 10:00:00", formatter));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-05-24 11:00:00", formatter));

        LectureTime saved = lectureTimeRepository.save(lectureTime);

        assertNotNull(saved.getId());
        assertEquals(LocalDateTime.parse("2025-05-24 10:00:00", formatter), saved.getStartLecture());
    }

    @Test
    void whenFindById_thenReturnLectureTime() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2025-05-24 12:00:00", formatter));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-05-24 13:00:00", formatter));
        lectureTimeRepository.save(lectureTime);

        Optional<LectureTime> found = lectureTimeRepository.findById(lectureTime.getId());

        assertTrue(found.isPresent());
        assertEquals(LocalDateTime.parse("2025-05-24 12:00:00", formatter), found.get().getStartLecture());
    }

    @Test
    void whenDeleteById_thenLectureTimeRemoved() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2025-05-24 14:00:00", formatter));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-05-24 15:00:00", formatter));
        lectureTimeRepository.save(lectureTime);

        lectureTimeRepository.deleteById(lectureTime.getId());

        Optional<LectureTime> found = lectureTimeRepository.findById(lectureTime.getId());
        assertFalse(found.isPresent());
    }

    @Test
    void whenCountByStartLectureAndEndLecture_thenReturnCount() {
        LocalDateTime start = LocalDateTime.parse("2025-05-24 16:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2025-05-24 17:00:00", formatter);

        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(start);
        lectureTime.setEndLecture(end);
        lectureTimeRepository.save(lectureTime);

        Long count = lectureTimeRepository.countByStartLectureAndEndLecture(start, end);

        assertEquals(1L, count);
    }

    @Test
    void whenNoLectureTimeWithGivenStartAndEnd_thenCountIsZero() {
        LocalDateTime start = LocalDateTime.parse("2025-05-24 18:00:00", formatter);
        LocalDateTime end = LocalDateTime.parse("2025-05-24 19:00:00", formatter);

        Long count = lectureTimeRepository.countByStartLectureAndEndLecture(start, end);

        assertEquals(0L, count);
    }
}

