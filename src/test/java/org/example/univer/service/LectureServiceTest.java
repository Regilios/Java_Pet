package org.example.univer.service;

import org.example.univer.config.AppSettings;
import org.example.univer.dto.LectureDto;
import org.example.univer.mappers.LectureMapper;
import org.example.univer.models.*;
import org.example.univer.repositories.HolidayRepository;
import org.example.univer.repositories.LectureRepository;
import org.example.univer.repositories.SubjectRepository;
import org.example.univer.repositories.TeacherRepository;
import org.example.univer.services.LectureService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
public class LectureServiceTest {
    @Mock
    private LectureMapper lectureMapper;
    @Mock
    private TeacherRepository teacherRepository;
    @Mock
    private LectureRepository lectureRepository;
    @Mock
    private SubjectRepository subjectRepository;
    @Mock
    private HolidayRepository holidayRepository;
    @Mock
    private AppSettings appSettings;

    private LectureService lectureService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(appSettings.getStartLectionDay()).thenReturn("08:00");
        when(appSettings.getEndLectionDay()).thenReturn("19:00");

        lectureService = spy(new LectureService(
                lectureRepository,
                teacherRepository,
                subjectRepository,
                holidayRepository,
                lectureMapper,
                appSettings
        ));
    }
    @Test
    void create_shouldSaveLecture_whenValid() {
        Lecture lecture = mock(Lecture.class);
        doReturn(null).when(lectureService).validate(eq(lecture), eq(LectureService.ValidationContext.METHOD_CREATE));

        lectureService.create(lecture);
        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void update_shouldSaveLecture_whenValid() {
        Lecture lecture = mock(Lecture.class);
        doReturn(null).when(lectureService).validate(eq(lecture), eq(LectureService.ValidationContext.METHOD_UPDATE));

        lectureService.update(lecture);
        verify(lectureRepository, times(1)).save(lecture);
    }

    @Test
    void deleteById_shouldDeleteLecture_whenExists() {
        Long id = 1L;
        Lecture lecture = new Lecture();
        Group group = new Group();

        lecture.setGroups(new ArrayList<>());
        lecture.getGroups().add(group);

        group.setLectures(new ArrayList<>());
        group.getLectures().add(lecture);

        when(lectureRepository.findByIdWithGroups(id)).thenReturn(Optional.of(lecture));

        lectureService.deleteById(id);

        verify(lectureRepository).delete(lecture);
    }

    @Test
    void findById_shouldReturnLectureDto_whenFound() {
        Long id = 1L;
        Lecture lecture = new Lecture();
        LectureDto dto = new LectureDto();

        when(lectureRepository.findById(id)).thenReturn(Optional.of(lecture));
        when(lectureMapper.toDto(lecture)).thenReturn(dto);

        LectureDto result = lectureService.findById(id);

        assertEquals(dto, result);
    }

    @Test
    void findAll_shouldReturnLectureList() {
        List<Lecture> lectures = List.of(new Lecture());
        when(lectureRepository.findAll()).thenReturn(lectures);

        List<Lecture> result = lectureService.findAll();

        assertEquals(lectures, result);
    }
    @Test
    void findAllWithGroups_shouldReturnPageOfDtos() {
        Lecture lecture = new Lecture();
        lecture.setId(1L);
        List<Lecture> lectures = List.of(lecture);

        Pageable pageable = PageRequest.of(0, 10);
        Page<Lecture> page = new PageImpl<>(lectures, pageable, 1);

        when(lectureRepository.findAllLectures(pageable)).thenReturn(page);
        when(lectureRepository.findWithGroupsByIdIn(List.of(1L))).thenReturn(lectures);
        when(lectureMapper.toDto(any())).thenReturn(new LectureDto());

        Page<LectureDto> result = lectureService.findAllWithGroups(pageable);

        assertEquals(1, result.getTotalElements());
    }

    @Test
    void isSingle_shouldReturnTrue_whenLectureExists() {
        Lecture lecture = mock(Lecture.class);
        Teacher teacher = mock(Teacher.class);
        Subject subject = mock(Subject.class);
        LectureTime lectureTime = mock(LectureTime.class);
        Audience audience = mock(Audience.class);

        when(teacher.getId()).thenReturn(1L);
        when(subject.getId()).thenReturn(2L);
        when(lectureTime.getId()).thenReturn(3L);
        when(audience.getId()).thenReturn(4L);

        when(lecture.getTeacher()).thenReturn(teacher);
        when(lecture.getSubject()).thenReturn(subject);
        when(lecture.getTime()).thenReturn(lectureTime);
        when(lecture.getAudience()).thenReturn(audience);

        when(lectureRepository.existsByTeacherIdAndSubjectIdAndTimeIdAndAudienceId(1L, 2L, 3L, 4L))
                .thenReturn(true);

        boolean result = lectureService.isSingle(lecture);

        assertTrue(result);
    }

    @Test
    void findByTeacherIdAndPeriod_shouldReturnLectures() {
        Teacher teacher = new Teacher();
        teacher.setId(1L);
        LocalDate start = LocalDate.of(2025, 5, 1);
        LocalDate end = LocalDate.of(2025, 5, 31);

        List<Lecture> lectures = List.of(new Lecture());

        when(lectureRepository.findLecturesByTeacherAndPeriod(
                start.atStartOfDay(), end.atStartOfDay(), teacher.getId())
        ).thenReturn(lectures);

        List<Lecture> result = lectureService.findByTeacherIdAndPeriod(teacher, start, end);

        assertEquals(lectures, result);
    }
}
