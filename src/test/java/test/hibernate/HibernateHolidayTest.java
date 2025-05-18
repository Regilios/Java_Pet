package test.hibernate;

import jakarta.persistence.EntityManager;
import org.example.univer.UniverApplication;
import org.example.univer.dao.hibernate.HibernateHoliday;
import org.example.univer.models.Holiday;
import org.hibernate.query.Query;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;
@SpringBootTest(classes = UniverApplication.class)
@ExtendWith(MockitoExtension.class)
public class HibernateHolidayTest {
    @Mock
    private EntityManager entityManager;
    @InjectMocks
    HibernateHoliday mockHoliday;

    @Test
    void whenCreateHoliday_thenHolidayIsPersisted() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);

        mockHoliday.create(holiday);
        verify(entityManager).persist(holiday);
    }

    @Test
    void whenUpdateHoliday_thenHolidayIsUpdated() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);

        mockHoliday.update(holiday);
        verify(entityManager).merge(holiday);
    }

    @Test
    void whenDeleteByIdGroup_thenHolidayIsRemoved() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);

        when(entityManager.find(Holiday.class, 1L)).thenReturn(holiday);
        mockHoliday.deleteById(1L);

        verify(entityManager).remove(holiday);
    }

    @Test
    void whenFindByIdHoliday_thenHolidayIsReturned() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);

        when(entityManager.find(Holiday.class, 1L)).thenReturn(holiday);
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
        when(entityManager.createNamedQuery("findAllHoliday", Holiday.class)).thenReturn(query);
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
        holiday.setStartHoliday(LocalDate.of(2025,10,10));
        holiday.setEndHoliday(LocalDate.of(2025,10,17));

        Query<Long> query = mock(Query.class);
        when(entityManager.createNamedQuery("findHolidayByDate", Long.class)).thenReturn(query);
        when(query.setParameter("date", holiday.getStartHoliday())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        boolean result = mockHoliday.lectureDoesNotFallOnHoliday(LocalDateTime.of(2025,10,10,0,0,0));
        assertTrue(result);
    }

    @Test
    void whenCheckIsSingleHoliday_thenReturnsCorrectResult() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);
        holiday.setDescription("test test test");

        Query<Long> query = mock(Query.class);
        when(entityManager.createNamedQuery("countHolidayByDescript", Long.class)).thenReturn(query);
        when(query.setParameter("description", holiday.getDescription())).thenReturn(query);
        when(query.getSingleResult()).thenReturn(1L);

        boolean result = mockHoliday.isSingle(holiday);
        assertTrue(result);
    }

}
