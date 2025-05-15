package test.hibernate;

import jakarta.persistence.EntityManager;
import org.example.univer.UniverApplication;
import org.example.univer.dao.hibernate.HibernateLecture;
import org.example.univer.models.*;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@SpringBootTest(classes = UniverApplication.class)
@ExtendWith(MockitoExtension.class)
public class HibernateLectureTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    HibernateLecture mockLecture;

    @Test
    void whenCreateLecture_thenLectureIsPersisted() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);

        mockLecture.create(lecture);
        verify(entityManager).persist(lecture);
    }

    @Test
    void whenUpdateLecture_thenLectureIsMerged() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);

        mockLecture.update(lecture);
        verify(entityManager).merge(lecture);
    }

    @Test
    void whenDeleteLecture_thenLectureIsRemoved() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);

        when(entityManager.find(Lecture.class, 1L)).thenReturn(lecture);
        mockLecture.deleteById(1L);

        verify(entityManager).remove(lecture);
    }

    @Test
    void whenFindLectureById_thenLectureIsReturned() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);

        Query<Lecture> query = mock(Query.class);
        when(entityManager.createNamedQuery("findLectureWidthGroups", Lecture.class)).thenReturn(query);
        when(query.setParameter("lecture_id", 1L)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(lecture);

        Optional<Lecture> result = mockLecture.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(lecture, result.get());
    }

    @Test
    void whenFindAllLectures_thenListOfLectureIsReturned() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);
        Lecture lecture2 = new Lecture();
        lecture2.setId(1L);

        Query<Lecture> query = mock(Query.class);

        when(entityManager.createNamedQuery("findAllLecture", Lecture.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(lecture, lecture2));

        List<Lecture> result = mockLecture.findAll();

        assertEquals(2, result.size());
        assertEquals(lecture, result.get(0));
        assertEquals(lecture2, result.get(1));
    }

    @Test
    void whenFindPaginatedLectures_thenPageOfLecturesIsReturned() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);
        Lecture lecture2 = new Lecture();
        lecture2.setId(1L);

        Pageable pageable = mock(Pageable.class);
        when(pageable.getOffset()).thenReturn(0L);
        when(pageable.getPageSize()).thenReturn(10);

        Query<Long> query = mock(Query.class);
        when(entityManager.createNamedQuery("countAllLecture", Long.class)).thenReturn(query);
        when(query.getSingleResult()).thenReturn(2L);

        Query<Lecture> queryLecture = mock(Query.class);
        when(entityManager.createNamedQuery("findAllLecturePaginatedWithGroups", Lecture.class)).thenReturn(queryLecture);
        when(queryLecture.setFirstResult(0)).thenReturn(queryLecture);
        when(queryLecture.setMaxResults(10)).thenReturn(queryLecture);
        when(queryLecture.getResultList()).thenReturn(List.of(lecture, lecture2));

        Page<Lecture> results = mockLecture.findPaginatedLecture(pageable);

        assertEquals(2, results.getTotalElements());
        assertEquals(2, results.getContent().size());
        assertEquals(lecture, results.getContent().get(0));
        assertEquals(lecture2, results.getContent().get(1));
    }

    @Test
    void whenCheckIsSingleLecture_thenReturnsCorrectResult() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        Subject subject = new Subject();
        subject.setId(1L);
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        Audience audience = new Audience();
        audience.setId(1L);

        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(lectureTime);
        lecture.setAudience(audience);


        Query<Long> query = mock(Query.class);
        when(entityManager.createNamedQuery("countLectureByParam", Long.class)).thenReturn(query);
        when(query.setParameter("teacher_id", lecture.getTeacher())).thenReturn(query);
        when(query.setParameter("subject_id", lecture.getSubject())).thenReturn(query);
        when(query.setParameter("lecture_time_id", lecture.getTime())).thenReturn(query);
        when(query.setParameter("audience_id", lecture.getAudience())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        boolean result = mockLecture.isSingle(lecture);
        assertTrue(result);
    }
    @Test
    void getTimetableTeacherForCreate_thenReturnsCorrectResult() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lecture.setTeacher(teacher);

        Lecture lecture2 = new Lecture();
        lecture2.setId(1L);
        lecture2.setTeacher(teacher);

        LocalDate localDate = LocalDate.of(2025,10,10);

        Query<Lecture> query = mock(Query.class);
        when(entityManager.createNamedQuery("getTimetableTeacherForCreate", Lecture.class)).thenReturn(query);
        when(query.setParameter("teacherId", teacher.getId())).thenReturn(query);
        when(query.setParameter("day_l", localDate.getDayOfMonth())).thenReturn(query);
        when(query.setParameter("month_l", localDate.getMonthValue())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(lecture, lecture2));

        List<Lecture> results = mockLecture.getTimetableTeacherForCreate(teacher,localDate);
        assertEquals(2, results.size());
        assertEquals(lecture, results.get(0));
        assertEquals(lecture2, results.get(1));
    }
}
