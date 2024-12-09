package org.example.univer.test.service;

import org.example.univer.dao.jdbc.JdbcLectureTime;
import org.example.univer.models.LectureTime;
import org.example.univer.services.LectureTimeService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LectionTimeServiceTest {
    @Mock
    private JdbcLectureTime mockJdbcLectureTime;

    @InjectMocks
    private LectureTimeService lectureTimeService;

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(lectureTimeService, "minimumLectureTimeMinutes", 30);
    }
    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @Test
    void create_lectionTimeDuration30min_and_correctTime_createLectionTime() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        lectureTime.setStart(LocalDateTime.parse("2024-02-01 14:30:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2024-02-01 16:30:00", formatter1));

        when(mockJdbcLectureTime.isSingle(lectureTime)).thenReturn(false);
        lectureTimeService.create(lectureTime);

        verify(mockJdbcLectureTime, times(1)).create(lectureTime);
    }

    @Test
    void create_lectionTimeDuration20min_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        lectureTime.setStart(LocalDateTime.parse("2024-02-01 14:00:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2024-02-01 14:20:00", formatter1));

        when(mockJdbcLectureTime.isSingle(lectureTime)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> {
            lectureTimeService.validate(lectureTime, LectureTimeService.ValidationContext.METHOD_CREATE);
            lectureTimeService.create(lectureTime);
        });
        verify(mockJdbcLectureTime, never()).create(any(LectureTime.class));
    }

    @Test
    void create_lectionTimeDuration30min_and_notCorrectTimePeriod_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        lectureTime.setStart(LocalDateTime.parse("2024-02-01 15:00:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2024-02-01 14:30:00", formatter1));

        when(mockJdbcLectureTime.isSingle(lectureTime)).thenReturn(false);
        assertThrows(IllegalArgumentException.class, () -> {
            lectureTimeService.validate(lectureTime, LectureTimeService.ValidationContext.METHOD_CREATE);
            lectureTimeService.create(lectureTime);
        });
        verify(mockJdbcLectureTime, never()).create(any(LectureTime.class));
    }

    @Test
    void isSingle_lecytionTimeIsSingle_true() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);

        when(mockJdbcLectureTime.isSingle(lectureTime)).thenReturn(true);
        assertTrue(lectureTimeService.isSingle(lectureTime));

        verify(mockJdbcLectureTime, times(1)).isSingle(any(LectureTime.class));
    }

    @Test
    void deleteById_deletedLectionTime_deleted() {
        lectureTimeService.deleteById(1L);

        verify(mockJdbcLectureTime, times(1)).deleteById(1L);
    }

    @Test
    void findById_findLectionTime_found() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        when(mockJdbcLectureTime.findById(1L)).thenReturn(lectureTime);
        LectureTime result = lectureTimeService.findById(1L);

        assertEquals(lectureTime, result);
    }

    @Test
    void findAll_findAllLectionTimes_foundAll() {
        List<LectureTime> groupsList = List.of(new LectureTime(), new LectureTime());

        when(mockJdbcLectureTime.findAll()).thenReturn(groupsList);
        List<LectureTime> result = lectureTimeService.findAll();

        assertEquals(groupsList, result);
    }
}
