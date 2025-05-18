package test.service;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoHolidayInterface;
import org.example.univer.dao.interfaces.DaoLectureInterface;
import org.example.univer.dao.interfaces.DaoSubjectInterface;
import org.example.univer.exeption.LectureExeption;
import org.example.univer.models.*;
import org.example.univer.services.GroupService;
import org.example.univer.services.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.Spy;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {
    @Spy
    private AppSettings appSettings = new AppSettings();
    @Mock
    private DaoLectureInterface mockLecture;
    @Mock
    private DaoSubjectInterface mockSubject;
    @Mock
    private GroupService groupService;
    @Mock
    private DaoHolidayInterface mockHoliday;

    private LectureService lectureService;

    private DateTimeFormatter formatter1 = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

    @BeforeEach
    void setUp() {
        appSettings.setStartLectionDay("08:00");
        appSettings.setEndLectionDay("19:00");
        lectureService = new LectureService( mockLecture, mockHoliday, mockSubject, groupService, appSettings);
    }

    @Test
    void create_LectureWidthCorrectData_createLecture() {
        Cathedra cathedra = new Cathedra();
        Teacher teacher = new Teacher();
        Subject subject = new Subject();

        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2025-02-02 14:30:00", formatter1));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-02-02 16:30:00", formatter1));

        Audience audience = new Audience();
        audience.setRoomNumber(1);
        audience.setCapacity(100);

        Lecture lecture = new Lecture();
        lecture.setCathedra(cathedra);
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(lectureTime);
        lecture.setAudience(audience);

        LectureTime mockLectureTime = new LectureTime();
        mockLectureTime.setStartLecture(LocalDateTime.parse("2025-03-02 14:30:00", formatter1));
        mockLectureTime.setEndLecture(LocalDateTime.parse("2025-03-02 16:30:00", formatter1));

        when(mockLecture.isSingle(lecture)).thenReturn(false);
        when(mockHoliday.lectureDoesNotFallOnHoliday(lecture.getTime().getStartLecture())).thenReturn(false);
        when(mockSubject.checkTeacherAssignedSubject(teacher, subject)).thenReturn(true);
        when(mockLecture.findByAudienceDateAndLectureTimeForCreate(lecture.getAudience(), lecture.getTime())).thenReturn(false);
        when(mockLecture.getTimetableTeacherForCreate(lecture.getTeacher(), LocalDate.from(lecture.getTime().getStartLecture()))).thenReturn(List.of(new Lecture() {{
            setTime(mockLectureTime);
        }}));

        lectureService.create(lecture);

        verify(mockLecture, times(1)).create(lecture);
    }

    @Test
    void create_LectureWidthIncorrectLectionTime_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2025-02-02 08:30:00", formatter1));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-02-02 20:30:00", formatter1));

        Lecture lecture = new Lecture();
        lecture.setTime(lectureTime);

        assertThrows(LectureExeption.class, () -> {
            lectureService.validate(lecture, LectureService.ValidationContext.METHOD_CREATE);
            lectureService.create(lecture);
        });
        verify(mockLecture, never()).create(any(Lecture.class));
    }

    @Test
    void create_LectureOnHolidays_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2024-01-02 08:30:00", formatter1));
        lectureTime.setEndLecture(LocalDateTime.parse("2024-01-02 10:30:00", formatter1));

        Lecture lecture = new Lecture();
        lecture.setTime(lectureTime);

        when(mockHoliday.lectureDoesNotFallOnHoliday(lecture.getTime().getStartLecture())).thenReturn(true);

        assertThrows(LectureExeption.class, () -> {
            lectureService.validate(lecture, LectureService.ValidationContext.METHOD_CREATE);
            lectureService.create(lecture);
        });
        verify(mockLecture, never()).create(any(Lecture.class));
    }

    @Test
    void create_TeacherDoesNotTeachSubject_throwException() {
        Teacher teacher = new Teacher();
        Subject subject = new Subject();

        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2025-02-02 10:30:00", formatter1));
        lectureTime.setEndLecture(LocalDateTime.parse("2025-02-02 12:30:00", formatter1));

        Lecture lecture = new Lecture();
        lecture.setTeacher(teacher);
        lecture.setSubject(subject);
        lecture.setTime(lectureTime);

        when(mockHoliday.lectureDoesNotFallOnHoliday(lecture.getTime().getStartLecture())).thenReturn(false);
        when(mockSubject.checkTeacherAssignedSubject(lecture.getTeacher(), lecture.getSubject())).thenReturn(false);

        assertThrows(LectureExeption.class, () -> {
            lectureService.validate(lecture, LectureService.ValidationContext.METHOD_CREATE);
            lectureService.create(lecture);
        });

        verify(mockLecture, never()).create(any(Lecture.class));
    }

    @Test
    void create_AudienceNotFree_throwException() {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setStartLecture(LocalDateTime.parse("2035-02-02 14:30:00", formatter1));
        lectureTime.setEndLecture(LocalDateTime.parse("2035-02-02 16:30:00", formatter1));

        Audience audience = new Audience();
        audience.setRoomNumber(1);
        audience.setCapacity(100);

        Teacher teacher = new Teacher();
        Subject subject = new Subject();

        Lecture lecture = new Lecture();
        lecture.setTime(lectureTime);
        lecture.setAudience(audience);
        lecture.setSubject(subject);
        lecture.setTeacher(teacher);

        when(mockLecture.isSingle(lecture)).thenReturn(false);
        when(mockLecture.findByAudienceDateAndLectureTimeForCreate(lecture.getAudience(), lecture.getTime())).thenReturn(true);

        assertThrows(LectureExeption.class, () -> {
            lectureService.validate(lecture, LectureService.ValidationContext.METHOD_CREATE);
            lectureService.create(lecture);
        });

        verify(mockLecture, never()).create(any(Lecture.class));
    }

    @Test
    void isSingle_lectureIsSingle_true() {
        Lecture lecture = new Lecture();

        when(mockLecture.isSingle(lecture)).thenReturn(true);
        assertTrue(lectureService.isSingle(lecture));

        verify(mockLecture, times(1)).isSingle(any(Lecture.class));
    }


    @Test
    void deleteById_deletedLecture_deleted() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);
        lectureService.deleteById(1L);

        verify(mockLecture, times(1)).deleteById(1L);
    }

    @Test
    void findById_findLecture_found() {
        Lecture lecture = new Lecture();

        when(mockLecture.findById(1L)).thenReturn(Optional.of(lecture));
        Optional<Lecture> result = lectureService.findById(1L);

        assertTrue(result.isPresent());
        assertEquals(lecture, result.get());
    }

    @Test
    void findAll_findAllLecture_foundAll() {
        List<Lecture> lectureList = List.of(new Lecture(), new Lecture());

        when(mockLecture.findAll()).thenReturn(lectureList);
        List<Lecture> result = lectureService.findAll();

        assertEquals(lectureList, result);
    }
}
