package org.example.univer.test.service;

import org.example.univer.dao.jdbc.*;
import org.example.univer.models.*;
import org.example.univer.services.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {
    @Mock
    private JdbcLecture mockJdbcLecture;
    @Mock
    private JdbcSubject mockJdbcSubject;
    @Mock
    private JdbcHoliday mockJdbcHoliday;
    @InjectMocks
    private LectureService lectureService;

    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        ReflectionTestUtils.setField(lectureService, "startLectionDay", LocalTime.parse("08:00"));
        ReflectionTestUtils.setField(lectureService, "endLectionDay", LocalTime.parse("19:00"));
    }

    @Test
    void create_LectureWidthCorrectData_createLecture() {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(1L);

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Subject subject = new Subject();
        subject.setId(1L);

        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(19L);
        lectureTime.setStart(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));


        Audience audience = new Audience();
        audience.setId(7L);
        audience.setRoom(1);
        audience.setCapacity(100);

        Lecture lecture = new Lecture();
        lecture.setId(17L);

        lecture.setCathedra(cathedra);
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(lectureTime);
        lecture.setAudience(audience);


        LectureTime mockLectureTime = new LectureTime();
        mockLectureTime.setStart(LocalDateTime.parse("2025-03-02 14:30:00", formatter1));
        mockLectureTime.setEnd(LocalDateTime.parse("2025-03-02 16:30:00", formatter1));

        when(mockJdbcLecture.isSingle(lecture)).thenReturn(false);
        when(mockJdbcHoliday.lectureDoesNotFallOnHoliday(lecture.getTime().getStartLocal())).thenReturn(false);
        when(mockJdbcSubject.checkTeacherAssignedSubject(teacher, subject)).thenReturn(true);
        when(mockJdbcLecture.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getTime())).thenReturn(false);
        when(mockJdbcLecture.getTimetableTeacher(lecture.getTeacher(), LocalDate.from(lecture.getTime().getStartLocal()))).thenReturn(List.of(new Lecture() {{
            setTime(mockLectureTime);
        }}));

        lectureService.create(lecture);

        verify(mockJdbcLecture, times(1)).create(lecture);
    }

    @Test
    void create_LectureWidthIncorrectLectionTime_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(19L);
        lectureTime.setStart(LocalDateTime.parse("2025-02-02 08:30:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2025-02-02 20:30:00", formatter1));

        Lecture lecture = new Lecture();
        lecture.setId(17L);
        lecture.setTime(lectureTime);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.validate(lecture, LectureService.ValidationContext.METHOD_CREATE);
            lectureService.create(lecture);
        });
        verify(mockJdbcLecture, never()).create(any(Lecture.class));
    }

    @Test
    void create_LectureOnHolidays_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(19L);
        lectureTime.setStart(LocalDateTime.parse("2024-01-02 08:30:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2024-01-02 10:30:00", formatter1));

        Lecture lecture = new Lecture();
        lecture.setId(17L);
        lecture.setTime(lectureTime);

        when(mockJdbcHoliday.lectureDoesNotFallOnHoliday(lecture.getTime().getStartLocal())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.validate(lecture, LectureService.ValidationContext.METHOD_CREATE);
            lectureService.create(lecture);
        });
        verify(mockJdbcLecture, never()).create(any(Lecture.class));
    }

    @Test
    void create_TeacherDoesNotTeachSubject_throwException() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Subject subject = new Subject();
        subject.setId(4L);

        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(19L);
        lectureTime.setStart(LocalDateTime.parse("2025-02-02 10:30:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2025-02-02 12:30:00", formatter1));

        Lecture lecture = new Lecture();
        lecture.setId(17L);
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(lectureTime);

        when(mockJdbcHoliday.lectureDoesNotFallOnHoliday(lecture.getTime().getStartLocal())).thenReturn(false);
        when(mockJdbcSubject.checkTeacherAssignedSubject(lecture.getTeacher(), lecture.getSubject())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.validate(lecture, LectureService.ValidationContext.METHOD_CREATE);
            lectureService.create(lecture);
        });

        verify(mockJdbcLecture, never()).create(any(Lecture.class));
    }

    @Test
    void create_AudienceNotFree_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(1L);
        lectureTime.setStart(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEnd(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));

        Audience audience = new Audience();
        audience.setId(1L);
        audience.setRoom(1);
        audience.setCapacity(100);

        Teacher teacher = new Teacher();
        teacher.setId(1L);

        Subject subject = new Subject();
        subject.setId(1L);

        Lecture lecture = new Lecture();
        lecture.setId(17L);

        lecture.setTime(lectureTime);
        lecture.setAudience(audience);
        lecture.setSubject(subject);
        lecture.setTeacher(teacher);

        when(mockJdbcLecture.isSingle(lecture)).thenReturn(false);
        when(mockJdbcHoliday.lectureDoesNotFallOnHoliday(lecture.getTime().getStartLocal())).thenReturn(false);
        when(mockJdbcSubject.checkTeacherAssignedSubject(teacher, subject)).thenReturn(true);
        when(mockJdbcLecture.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getTime())).thenReturn(true);

        assertThrows(IllegalArgumentException.class, () -> {
            lectureService.validate(lecture, LectureService.ValidationContext.METHOD_CREATE);
            lectureService.create(lecture);
        });

        verify(mockJdbcLecture, never()).create(any(Lecture.class));
    }

    @Test
    void isSingle_lectureIsSingle_true() {
        Lecture lecture = new Lecture();
        lecture.setId(17L);

        when(mockJdbcLecture.isSingle(lecture)).thenReturn(true);
        assertTrue(lectureService.isSingle(lecture));

        verify(mockJdbcLecture, times(1)).isSingle(any(Lecture.class));
    }


    @Test
    void deleteById_deletedLecture_deleted() {
        lectureService.deleteById(1L);

        verify(mockJdbcLecture, times(1)).deleteById(1L);
    }

    @Test
    void findById_findLecture_found() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);

        when(mockJdbcLecture.findById(1L)).thenReturn(lecture);
        Lecture result = lectureService.findById(1L);

        assertEquals(lecture, result);
    }

    @Test
    void findAll_findAllLecture_foundAll() {
        List<Lecture> lectureList = List.of(new Lecture(), new Lecture());

        when(mockJdbcLecture.findAll()).thenReturn(lectureList);
        List<Lecture> result = lectureService.findAll();

        assertEquals(lectureList, result);
    }
}
