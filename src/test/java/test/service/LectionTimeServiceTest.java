package test.service;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoLectureTimeInterface;
import org.example.univer.exeption.LectureTimeExeption;
import org.example.univer.models.LectureTime;
import org.example.univer.services.LectureTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class LectionTimeServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private DaoLectureTimeInterface mockLectureTime;
    @InjectMocks
    private LectureTimeService lectureTimeService;

    @BeforeEach
    void setUp() {
        appSettings.setMinimumLectureTimeMinutes(30);
        lectureTimeService = new LectureTimeService(mockLectureTime, appSettings);
    }

    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void create_lectionTimeDuration30min_and_correctTime_createLectionTime() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLection(LocalDateTime.parse("2024-02-01 14:30:00", formatter1));
        lectureTime.setEndLection(LocalDateTime.parse("2024-02-01 16:30:00", formatter1));

        when(mockLectureTime.isSingle(lectureTime)).thenReturn(false);
        lectureTimeService.create(lectureTime);

        verify(mockLectureTime, times(1)).create(lectureTime);
    }

    @Test
    void create_lectionTimeDuration20min_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLection(LocalDateTime.parse("2024-02-01 14:00:00", formatter1));
        lectureTime.setEndLection(LocalDateTime.parse("2024-02-01 14:20:00", formatter1));

        when(mockLectureTime.isSingle(lectureTime)).thenReturn(false);
        assertThrows(LectureTimeExeption.class, () -> {
            lectureTimeService.validate(lectureTime, LectureTimeService.ValidationContext.METHOD_CREATE);
            lectureTimeService.create(lectureTime);
        });
        verify(mockLectureTime, never()).create(any(LectureTime.class));
    }

    @Test
    void create_lectionTimeDuration30min_and_notCorrectTimePeriod_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLection(LocalDateTime.parse("2024-02-01 15:00:00", formatter1));
        lectureTime.setEndLection(LocalDateTime.parse("2024-02-01 14:30:00", formatter1));

        when(mockLectureTime.isSingle(lectureTime)).thenReturn(false);
        assertThrows(LectureTimeExeption.class, () -> {
            lectureTimeService.validate(lectureTime, LectureTimeService.ValidationContext.METHOD_CREATE);
            lectureTimeService.create(lectureTime);
        });
        verify(mockLectureTime, never()).create(any(LectureTime.class));
    }

    @Test
    void isSingle_lecytionTimeIsSingle_true() {
        LectureTime lectureTime = new LectureTime();

        when(mockLectureTime.isSingle(lectureTime)).thenReturn(true);
        assertTrue(lectureTimeService.isSingle(lectureTime));

        verify(mockLectureTime, times(1)).isSingle(any(LectureTime.class));
    }

    @Test
    void deleteById_deletedLectionTime_deleted() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        lectureTimeService.deleteById(1L);

        verify(mockLectureTime, times(1)).deleteById(1L);
    }

    @Test
    void findById_findLectionTime_found() {
        LectureTime lectureTime = new LectureTime();

        when(mockLectureTime.findById(1L)).thenReturn(Optional.of(lectureTime));
        Optional<LectureTime> result = lectureTimeService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(lectureTime, result.get());
    }

    @Test
    void findAll_findAllLectionTimes_foundAll() {
        List<LectureTime> groupsList = List.of(new LectureTime(), new LectureTime());

        when(mockLectureTime.findAll()).thenReturn(groupsList);
        List<LectureTime> result = lectureTimeService.findAll();

        assertEquals(groupsList, result);
    }
}
