package org.example.univer.service;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoHolidayInterface;
import org.example.univer.exeption.HolidaysExeption;
import org.example.univer.models.Holiday;
import org.example.univer.services.HolidayService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class HolidayServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private DaoHolidayInterface mockHoliday;
    @InjectMocks
    private HolidayService holidayService;

    @BeforeEach
    void setUp() {
        appSettings.setMaxDayHoliday(14);
        appSettings.setStartDayHoliday("MONDAY");
        holidayService = new HolidayService(mockHoliday, appSettings);
    }

    @Test
    void create_holiday10dayStartMonday_createHoliday() {
        Holiday holiday = new Holiday();
        holiday.setDescription("test");
        holiday.setStartHoliday(LocalDate.parse("2024-01-01"));
        holiday.setEndHoliday(LocalDate.parse("2024-01-14"));

        when(mockHoliday.isSingle(holiday)).thenReturn(false);
        holidayService.create(holiday);

        verify(mockHoliday, times(1)).create(holiday);
    }

    @Test
    void create_holiday10dayStartThursday_throwException() {
        Holiday holiday = new Holiday();
        holiday.setDescription("test");
        holiday.setStartHoliday(LocalDate.parse("2024-01-02"));
        holiday.setEndHoliday(LocalDate.parse("2024-01-14"));

        when(mockHoliday.isSingle(holiday)).thenReturn(false);
        assertThrows(HolidaysExeption.class, () -> {
            holidayService.validate(holiday, HolidayService.ValidationContext.METHOD_CREATE);
            holidayService.create(holiday);
        });
        verify(mockHoliday, never()).create(any(Holiday.class));
    }

    @Test
    void isSingle_holidayIsSingle_true() {
        Holiday holiday = new Holiday();

        when(mockHoliday.isSingle(holiday)).thenReturn(true);
        assertTrue(holidayService.isSingle(holiday));

        verify(mockHoliday, times(1)).isSingle(any(Holiday.class));
    }

    @Test
    void deleteById_deletedHoliday_deleted() {
        Holiday holiday = new Holiday();
        holiday.setId(1L);
        holidayService.deleteById(1L);

        verify(mockHoliday, times(1)).deleteById(1L);
    }

    @Test
    void findById_findHoliday_found() {
        Holiday holiday = new Holiday();

        when(mockHoliday.findById(1L)).thenReturn(Optional.of(holiday));
        Optional<Holiday> result = holidayService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(holiday, result.get());
    }

    @Test
    void findAll_findAllHolidays_foundAll() {
        List<Holiday> groupsList = List.of(new Holiday(), new Holiday());

        when(mockHoliday.findAll()).thenReturn(groupsList);
        List<Holiday> result = holidayService.findAll();

        assertEquals(groupsList, result);
    }
}
