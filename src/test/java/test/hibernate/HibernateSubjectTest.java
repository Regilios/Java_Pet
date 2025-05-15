package test.hibernate;

import org.example.univer.dao.hibernate.HibernateSubject;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HibernateSubjectTest {
    @Mock
    Session session;
    @Mock
    SessionFactory sessionFactory;
    @InjectMocks
    HibernateSubject mockSubject;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void whenCreateSubject_thenSubjectIsPersisted() {
        Subject subject = new Subject();
        subject.setId(1L);

        mockSubject.create(subject);
        verify(session).persist(subject);
    }

    @Test
    void whenUpdateSubject_thenSubjectIsUpdated() {
        Subject subject = new Subject();
        subject.setId(1L);

        mockSubject.update(subject);
        verify(session).merge(subject);
    }

    @Test
    void whenDeleteByIdSubject_thenSubjectIsRemoved() {
        Subject subject = new Subject();
        subject.setId(1L);

        mockSubject.deleteEntity(subject);
        verify(session).remove(subject);
    }


    @Test
    void whenFindByIdSubject_thenSubjectIsReturned() {
        Subject subject = new Subject();
        subject.setId(1L);

        when(session.get(Subject.class, 1L)).thenReturn(subject);
        Optional<Subject> result = mockSubject.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(subject, result.get());
    }

    @Test
    void whenFindAllSubject_thenListOfSubjectIsReturned() {
        Subject subject = new Subject();
        subject.setId(1L);
        Subject subject2 = new Subject();
        subject2.setId(2L);

        Query<Subject> query = mock(Query.class);
        when(session.createNamedQuery("findAllSubjects", Subject.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(subject,subject2));

        List<Subject> result = mockSubject.findAll();

        assertEquals(2, result.size());
        assertEquals(subject, result.get(0));
        assertEquals(subject2, result.get(1));
    }

    @Test
    void whenCheckIsSingleSubject_thenReturnsCorrectResult() {
        Subject subject = new Subject();
        subject.setId(1L);
        subject.setName("test");

        Query<Long> query = mock(Query.class);
        when(session.createNamedQuery("countAllSubjects", Long.class)).thenReturn(query);
        when(query.setParameter("name", subject.getName())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1L);

        boolean result = mockSubject.isSingle(subject);
        assertTrue(result);
    }

    @Test
    void checkTeacherAssignedSubject_thenReturnsCorrectResult() {
        Subject subject = new Subject();
        subject.setId(1L);
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Query<Long> query = mock(Query.class);
        when(session.createNamedQuery("checkTeacherAssignedSubject", Long.class)).thenReturn(query);
        when(query.setParameter("teacher_id", 1L)).thenReturn(query);
        when(query.setParameter("subject_id", 1L)).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1L);

        boolean result = mockSubject.checkTeacherAssignedSubject(teacher,subject);
        assertTrue(result);
    }

    @Test
    void getSubjectById_thenReturnsCorrectResult() {
        Subject subject = new Subject();
        subject.setId(1L);

        Query<Subject> query = mock(Query.class);
        when(session.createNamedQuery("findSubjectsByTeacherId", Subject.class)).thenReturn(query);
        when(query.setParameter("teacher_id", 1L)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(subject));

        List<Subject> result = mockSubject.getSubjectById(1L);

        assertEquals(1, result.size());
        assertEquals(subject, result.get(0));
    }
}
