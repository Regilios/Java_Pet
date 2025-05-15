package test.service;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoVacationInterface;
import org.example.univer.exeption.VacationExeption;
import org.example.univer.models.Teacher;
import org.example.univer.models.Vacation;
import org.example.univer.services.VacationService;
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
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class VacationServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private DaoVacationInterface mockVacation;
    @InjectMocks
    private VacationService vacationService;

    @BeforeEach
    void setUp() {
        appSettings.setMinVacationDay(7);
        appSettings.setMaxVacationDay(20);
        vacationService = new VacationService(mockVacation, appSettings);
    }

    @Test
    void create_vacationCorrectData_createTeacher() {
        Teacher teacher = new Teacher();

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2024-07-01"));
        vacation.setEndJob(LocalDate.parse("2024-07-14"));
        vacation.setTeacher(teacher);

        when(mockVacation.isSingle(vacation)).thenReturn(false);
        vacationService.create(vacation);

        verify(mockVacation, times(1)).create(vacation);
    }

    @Test
    void create_vacationNotCorrectData_throwException() {
        Teacher teacher = new Teacher();

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2024-07-11"));
        vacation.setEndJob(LocalDate.parse("2024-07-01"));
        vacation.setTeacher(teacher);

        when(mockVacation.isSingle(vacation)).thenReturn(false);

        assertThrows(VacationExeption.class, () -> {
            vacationService.validate(vacation, VacationService.ValidationContext.METHOD_CREATE);
            vacationService.create(vacation);
        });
        verify(mockVacation, never()).create(any(Vacation.class));
    }

    @Test
    void create_exceedingMaximumNumberVacationDays_throwException() {
        Teacher teacher = new Teacher();

        Vacation vacation = new Vacation();
        vacation.setStartJob(LocalDate.parse("2024-07-01"));
        vacation.setEndJob(LocalDate.parse("2024-07-30"));
        vacation.setTeacher(teacher);

        when(mockVacation.isSingle(vacation)).thenReturn(false);

        assertThrows(VacationExeption.class, () -> {
            vacationService.validate(vacation, VacationService.ValidationContext.METHOD_CREATE);
            vacationService.create(vacation);
        });
        verify(mockVacation, never()).create(any(Vacation.class));
    }

    @Test
    void isSingle_vacationIsSingle_true() {
        Vacation vacation = new Vacation();

        when(mockVacation.isSingle(vacation)).thenReturn(true);
        assertTrue(vacationService.isSingle(vacation));

        verify(mockVacation, times(1)).isSingle(any(Vacation.class));
    }

    @Test
    void deleteById_deletedVacation_deleted() {
        Vacation vacation = new Vacation();
        vacation.setId(1L);
        vacationService.deleteById(1L);

        verify(mockVacation, times(1)).deleteById(1L);
    }

    @Test
    void findById_findVacation_found() {
        Vacation vacation = new Vacation();

        when(mockVacation.findById(1L)).thenReturn(Optional.of(vacation));
        Optional<Vacation> result = vacationService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(vacation, result.get());
    }

    @Test
    void findAll_findAllAudiences_foundAll() {
        List<Vacation> vacationList = List.of(new Vacation(), new Vacation());

        when(mockVacation.findAll()).thenReturn(vacationList);
        List<Vacation> result = vacationService.findAll();

        assertEquals(vacationList, result);
    }
}
