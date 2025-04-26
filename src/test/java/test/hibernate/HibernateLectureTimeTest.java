package test.hibernate;

import org.example.univer.dao.hibernate.HibernateLectureTime;
import org.example.univer.models.LectureTime;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HibernateLectureTimeTest {
    @Mock
    private Session session;
    @Mock
    private SessionFactory sessionFactory;
    @InjectMocks
    private HibernateLectureTime mockLectureTime;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void whenCreateLectureTime_thenLectureTimeIsPersisted() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);

        mockLectureTime.create(lectureTime);

        verify(session).persist(lectureTime);
    }

    @Test
    void whenUpdateLectureTime_thenLectureTimeIsMerged() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);

        mockLectureTime.update(lectureTime);

        verify(session).merge(lectureTime);
    }

    @Test
    void whenDeleteLectureTime_thenLectureTimeIsRemoved() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);

        mockLectureTime.deleteById(lectureTime);

        verify(session).remove(lectureTime);
    }

    @Test
    void whenFindByIdLectureTime_thenLectureTimeIsReturned() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);

        when(session.get(LectureTime.class, 1L)).thenReturn(lectureTime);

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
        when(session.createNamedQuery("findAllLectureTime", LectureTime.class)).thenReturn(query);
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
        lectureTime.setStart_lection(LocalDateTime.of(2025,10,10,10,0,0));
        lectureTime.setEnd_lection(LocalDateTime.of(2025,10,10,11,0,0));

        Query<Long> query = mock(Query.class);
        when(session.createNamedQuery("findLectureTime", Long.class)).thenReturn(query);
        when(query.setParameter("start_lection", lectureTime.getStartLocal())).thenReturn(query);
        when(query.setParameter("end_lection", lectureTime.getEndLocal())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1L);

        boolean result =mockLectureTime.isSingle(lectureTime);
        assertTrue(result);

    }
}
