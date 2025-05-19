package org.example.univer.hibernate;

import jakarta.persistence.EntityManager;
import org.example.univer.UniverApplication;
import org.example.univer.dao.hibernate.HibernateLectureTime;
import org.example.univer.models.LectureTime;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@SpringBootTest(classes = UniverApplication.class)
@ExtendWith(MockitoExtension.class)
public class HibernateLectureTimeTest {

    @Mock
    private EntityManager entityManager;
    @InjectMocks
    private HibernateLectureTime mockLectureTime;

    @Test
    void whenCreateLectureTime_thenLectureTimeIsPersisted() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);

        mockLectureTime.create(lectureTime);
        verify(entityManager).persist(lectureTime);
    }

    @Test
    void whenUpdateLectureTime_thenLectureTimeIsMerged() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);

        mockLectureTime.update(lectureTime);
        verify(entityManager).merge(lectureTime);
    }

    @Test
    void whenDeleteLectureTime_thenLectureTimeIsRemoved() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);

        when(entityManager.find(LectureTime.class, 1L)).thenReturn(lectureTime);
        mockLectureTime.deleteById(1L);

        verify(entityManager).remove(lectureTime);
    }

    @Test
    void whenFindByIdLectureTime_thenLectureTimeIsReturned() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);

        when(entityManager.find(LectureTime.class, 1L)).thenReturn(lectureTime);

        Optional<LectureTime> result = mockLectureTime.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(lectureTime, result.get());
    }

    @Test
    void whenFindAllLectureTime_thenListOfLectureTimeIsReturned() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        LectureTime lectureTime2 = new LectureTime();
        lectureTime2.setId(2L);

        Query<LectureTime> query = mock(Query.class);
        when(entityManager.createNamedQuery("findAllLectureTime", LectureTime.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(lectureTime,lectureTime2));

        List<LectureTime> result = mockLectureTime.findAll();

        assertEquals(2 , result.size());
        assertEquals(lectureTime, result.get(0));
        assertEquals(lectureTime2, result.get(1));
    }

    @Test
    void whenCheckIsSingleLectureTime_thenReturnsCorrectResult() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        lectureTime.setStartLecture(LocalDateTime.of(2025,10,10,10,0,0));
        lectureTime.setEndLecture(LocalDateTime.of(2025,10,10,11,0,0));

        Query<Long> query = mock(Query.class);
        when(entityManager.createNamedQuery("findLectureTime", Long.class)).thenReturn(query);
        when(query.setParameter("startLecture", lectureTime.getStartLecture())).thenReturn(query);
        when(query.setParameter("endLecture", lectureTime.getEndLecture())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        boolean result =mockLectureTime.isSingle(lectureTime);
        assertTrue(result);

    }
}
