package org.example.univer.test.service;

import org.example.univer.dao.jdbc.JdbcHoliday;
import org.example.univer.exeption.HolidaysExeption;
import org.example.univer.models.Holiday;
import org.example.univer.services.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class HolidayServiceTest {
    @Mock
    private JdbcHoliday mockJdbcHoliday;

    @InjectMocks
    private HolidayService holidayService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(holidayService, "maxDayHoliday", 14);
        ReflectionTestUtils.setField(holidayService, "startDayHoliday", "MONDAY");
    }

    @Test
    void create_holiday10dayStartMonday_createHoliday() {
        Holiday holiday = new Holiday();
        holiday.setDesc("test");
        holiday.setStart_holiday(LocalDate.parse("2024-01-01"));
        holiday.setEnd_holiday(LocalDate.parse("2024-01-14"));

        when(mockJdbcHoliday.isSingle(holiday)).thenReturn(false);
        holidayService.create(holiday);

        verify(mockJdbcHoliday, times(1)).create(holiday);
    }

    @Test
    void create_holiday10dayStartThursday_throwException() {
        Holiday holiday = new Holiday();
        holiday.setDesc("test");
        holiday.setStart_holiday(LocalDate.parse("2024-01-02"));
        holiday.setEnd_holiday(LocalDate.parse("2024-01-14"));

        when(mockJdbcHoliday.isSingle(holiday)).thenReturn(false);
        assertThrows(HolidaysExeption.class, () -> {
            holidayService.validate(holiday, HolidayService.ValidationContext.METHOD_CREATE);
            holidayService.create(holiday);
        });
        verify(mockJdbcHoliday, never()).create(any(Holiday.class));
    }

    @Test
    void isSingle_holidayIsSingle_true() {
        Holiday holiday = new Holiday();

        when(mockJdbcHoliday.isSingle(holiday)).thenReturn(true);
        assertTrue(holidayService.isSingle(holiday));

        verify(mockJdbcHoliday, times(1)).isSingle(any(Holiday.class));
    }

    @Test
    void deleteById_deletedHoliday_deleted() {
        holidayService.deleteById(1L);

        verify(mockJdbcHoliday, times(1)).deleteById(1L);
    }

    @Test
    void findById_findHoliday_found() {
        Holiday holiday = new Holiday();

        when(mockJdbcHoliday.findById(1L)).thenReturn(holiday);
        Holiday result = holidayService.findById(1L);

        assertEquals(holiday, result);
    }

    @Test
    void findAll_findAllHolidays_foundAll() {
        List<Holiday> groupsList = List.of(new Holiday(), new Holiday());

        when(mockJdbcHoliday.findAll()).thenReturn(groupsList);
        List<Holiday> result = holidayService.findAll();

        assertEquals(groupsList, result);
    }
}
