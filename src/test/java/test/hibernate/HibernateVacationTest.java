package test.hibernate;

import org.example.univer.dao.hibernate.HibernateVacation;
import org.example.univer.models.Teacher;
import org.example.univer.models.Vacation;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.query.Query;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HibernateVacationTest {
    @Mock
    Session session;
    @Mock
    SessionFactory sessionFactory;
    @InjectMocks
    HibernateVacation mockVacation;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void whenCreateVacation_thenVacationIsPersisted() {
        Vacation vacation = new Vacation();
        vacation.setId(1L);

        mockVacation.create(vacation);
        verify(session).persist(vacation);
    }

    @Test
    void whenUpdateVacation_thenVacationIsUpdated() {
        Vacation vacation = new Vacation();
        vacation.setId(1L);

        mockVacation.update(vacation);
        verify(session).merge(vacation);
    }

    @Test
    void whenDeleteByIdVacation_thenVacationIsRemoved() {
        Vacation vacation = new Vacation();
        vacation.setId(1L);

        mockVacation.deleteById(vacation);
        verify(session).remove(vacation);
    }

    @Test
    void whenFindByIdVacation_thenVacationIsReturned() {
        Vacation vacation = new Vacation();
        vacation.setId(1L);

        when(session.get(Vacation.class, 1L)).thenReturn(vacation);
        Optional<Vacation> result = mockVacation.findById(1L);
        assertTrue(result.isPresent());
        assertEquals(vacation, result.get());
    }

    @Test
    void whenFindAllVacation_thenListOfVacationIsReturned() {
        Vacation vacation = new Vacation();
        vacation.setId(1L);
        Vacation vacation2 = new Vacation();
        vacation2.setId(1L);

        Query<Vacation> query = mock(Query.class);
        when(session.createNamedQuery("findAllVacation", Vacation.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(vacation, vacation2));

        List<Vacation> result = mockVacation.findAll();

        assertEquals(2, result.size());
        assertEquals(vacation, result.get(0));
        assertEquals(vacation2, result.get(1));
    }

    @Test
    void whenCheckIsSingleVacation_thenReturnsCorrectResult() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Vacation vacation = new Vacation();
        vacation.setId(1L);
        vacation.setStartJob(LocalDate.of(2025,10,10));
        vacation.setEndJob(LocalDate.of(2025,10,12));
        vacation.setTeacher(teacher);


        Query<Long> query = mock(Query.class);
        when(session.createNamedQuery("countVacantion", Long.class)).thenReturn(query);
        when(query.setParameter("startJob", vacation.getStartJob())).thenReturn(query);
        when(query.setParameter("endJob", vacation.getEndJob())).thenReturn(query);
        when(query.setParameter("teacher_id", vacation.getTeacher().getId())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1L);

        boolean result = mockVacation.isSingle(vacation);
        assertTrue(result);
    }

    @Test
    void findByTeacherId_thenListOfVacationIsReturned() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Vacation vacation = new Vacation();
        vacation.setId(1L);
        vacation.setTeacher(teacher);

        Query<Vacation> query = mock(Query.class);
        when(session.createNamedQuery("findListVacantion", Vacation.class)).thenReturn(query);
        when(query.setParameter("teacher_id", vacation.getTeacher().getId())).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(vacation));

        List<Vacation> result = mockVacation.findByTeacherId(1L);

        assertEquals(1, result.size());
        assertEquals(vacation, result.get(0));
    }

}
