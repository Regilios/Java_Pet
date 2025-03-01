package org.example.univer.test.service;

import org.example.univer.dao.jdbc.JdbcVacation;
import org.example.univer.exeption.VacationExeption;
import org.example.univer.models.Teacher;
import org.example.univer.models.Vacation;
import org.example.univer.services.VacationService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class VacationServiceTest {
    @Mock
    private JdbcVacation mockJdbcVacation;
    @InjectMocks
    private VacationService vacationService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        ReflectionTestUtils.setField(vacationService, "minVacationDay", 7);
        ReflectionTestUtils.setField(vacationService, "maxVacationDay", 20);
    }

    @Test
    void create_vacationCorrectData_createTeacher() {
        Teacher teacher = new Teacher();

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2024-07-01"));
        vacation.setEndJob(LocalDate.parse("2024-07-14"));
        vacation.setTeacher(teacher);

        when(mockJdbcVacation.isSingle(vacation)).thenReturn(false);
        vacationService.create(vacation);

        verify(mockJdbcVacation, times(1)).create(vacation);
    }

    @Test
    void create_vacationNotCorrectData_throwException() {
        Teacher teacher = new Teacher();

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2024-07-11"));
        vacation.setEndJob(LocalDate.parse("2024-07-01"));
        vacation.setTeacher(teacher);

        when(mockJdbcVacation.isSingle(vacation)).thenReturn(false);

        assertThrows(VacationExeption.class, () -> {
            vacationService.validate(vacation, VacationService.ValidationContext.METHOD_CREATE);
            vacationService.create(vacation);
        });
        verify(mockJdbcVacation, never()).create(any(Vacation.class));
    }

    @Test
    void create_exceedingMaximumNumberVacationDays_throwException() {
        Teacher teacher = new Teacher();

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2024-07-01"));
        vacation.setEndJob(LocalDate.parse("2024-07-30"));
        vacation.setTeacher(teacher);

        when(mockJdbcVacation.isSingle(vacation)).thenReturn(false);

        assertThrows(VacationExeption.class, () -> {
            vacationService.validate(vacation, VacationService.ValidationContext.METHOD_CREATE);
            vacationService.create(vacation);
        });
        verify(mockJdbcVacation, never()).create(any(Vacation.class));
    }

    @Test
    void isSingle_vacationIsSingle_true() {
        Vacation vacation = new Vacation();

        when(mockJdbcVacation.isSingle(vacation)).thenReturn(true);
        assertTrue(vacationService.isSingle(vacation));

        verify(mockJdbcVacation, times(1)).isSingle(any(Vacation.class));
    }

    @Test
    void deleteById_deletedVacation_deleted() {
        vacationService.deleteById(1L);

        verify(mockJdbcVacation, times(1)).deleteById(1L);
    }

    @Test
    void findById_findVacation_found() {
        Vacation vacation = new Vacation();

        when(mockJdbcVacation.findById(1L)).thenReturn(vacation);
        Vacation result = vacationService.findById(1L);

        assertEquals(vacation, result);
    }

    @Test
    void findAll_findAllAudiences_foundAll() {
        List<Vacation> vacationList = List.of(new Vacation(), new Vacation());

        when(mockJdbcVacation.findAll()).thenReturn(vacationList);
        List<Vacation> result = vacationService.findAll();

        assertEquals(vacationList, result);
    }
}
