package test.hibernate;

import org.example.univer.dao.hibernate.HibernateHoliday;
import org.example.univer.models.Holiday;
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
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class HibernateHolidayTest {
    @Mock
    Session session;
    @Mock
    SessionFactory sessionFactory;
    @InjectMocks
    HibernateHoliday mockHoliday;

    @BeforeEach
    void setUp() {
        when(sessionFactory.getCurrentSession()).thenReturn(session);
    }

    @Test
    void whenCreateHoliday_thenHolidayIsPersisted() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);

        mockHoliday.create(holiday);
        verify(session).persist(holiday);
    }

    @Test
    void whenUpdateHoliday_thenHolidayIsUpdated() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);

        mockHoliday.update(holiday);
        verify(session).merge(holiday);
    }

    @Test
    void whenDeleteByIdGroup_thenHolidayIsRemoved() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);

        mockHoliday.deleteById(holiday);
        verify(session).remove(holiday);
    }

    @Test
    void whenFindByIdHoliday_thenHolidayIsReturned() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);

        when(session.get(Holiday.class, 1L)).thenReturn(holiday);
        Optional<Holiday> result = mockHoliday.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(holiday, result.get());
    }

    @Test
    void whenFindAllHoliday_thenListOfHolidayIsReturned() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);
        Holiday holiday2 = new Holiday();
        holiday2.setId(2L);

        Query<Holiday> query = mock(Query.class);
        when(session.createNamedQuery("findAllHoliday", Holiday.class)).thenReturn(query);
        when(query.getResultList()).thenReturn(List.of(holiday,holiday2));

        List<Holiday> result = mockHoliday.findAll();

        assertEquals(2, result.size());
        assertEquals(holiday, result.get(0));
        assertEquals(holiday2, result.get(1));
    }

    @Test
    void whenLectureDoesNotFallOnHoliday_thenReturnsCorrectResult() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);
        holiday.setStart_holiday(LocalDate.of(2025,10,10));
        holiday.setEnd_holiday(LocalDate.of(2025,10,17));

        Query<Long> query = mock(Query.class);
        when(session.createNamedQuery("findHolidayByDate", Long.class)).thenReturn(query);
        when(query.setParameter("date", holiday.getStart_holiday())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1L);

        boolean result = mockHoliday.lectureDoesNotFallOnHoliday(LocalDateTime.of(2025,10,10,0,0,0));
        assertTrue(result);
    }

    @Test
    void whenCheckIsSingleHoliday_thenReturnsCorrectResult() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);
        holiday.setDescription("test test test");

        Query<Long> query = mock(Query.class);
        when(session.createNamedQuery("countHolidayByDescript", Long.class)).thenReturn(query);
        when(query.setParameter("description", holiday.getDescription())).thenReturn(query);
        when(query.uniqueResult()).thenReturn(1L);

        boolean result = mockHoliday.isSingle(holiday);
        assertTrue(result);
    }

}
