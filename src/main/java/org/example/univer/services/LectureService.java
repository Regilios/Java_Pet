package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.dto.LectureDto;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.exeption.LectureExeption;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.mappers.LectureMapper;
import org.example.univer.models.Lecture;
import org.example.univer.models.Teacher;
import org.example.univer.repositories.HolidayRepository;
import org.example.univer.repositories.LectureRepository;
import org.example.univer.repositories.SubjectRepository;
import org.example.univer.repositories.TeacherRepository;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class LectureService {
    private final LectureRepository lectureRepository;
    private final TeacherRepository teacherRepository;
    private final SubjectRepository subjectRepository;
    private final HolidayRepository holidayRepository;
    private final LectureMapper lectureMapper;
    private final AppSettings appSettings;

    private static final Logger logger = LoggerFactory.getLogger(LectureService.class);
    private LocalTime startLectionDay;
    private LocalTime endLectionDay;

    @PostConstruct
    public void init() {
        this.startLectionDay = LocalTime.parse(appSettings.getStartLectionDay(), DateTimeFormatter.ofPattern("HH:mm"));
        this.endLectionDay = LocalTime.parse(appSettings.getEndLectionDay(), DateTimeFormatter.ofPattern("HH:mm"));
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Lecture lecture, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(lecture)) {
                    throw new InvalidParameterException("Лекция с такими параметрами уже существует!");
                }
                if (isAudienceFreeForCreate(lecture)) {
                    throw new LectureExeption(String.format("Аудитория %s уже занята на время %s",
                            lecture.getAudience().getRoomNumber(), lecture.getTime().getStartLecture()));
                }
                if (!isTeacherBusyForCreate(lecture)) {
                    throw new LectureExeption(String.format("Учитель уже занят на время %s",
                            lecture.getTime().getStartLecture()));
                }
                validateCommon(lecture, "создать");
                break;
            case METHOD_UPDATE:
                if (isAudienceFreeForUpdate(lecture)) {
                    throw new LectureExeption(String.format("Аудитория %s уже занята на время %s",
                            lecture.getAudience().getRoomNumber(), lecture.getTime().getStartLecture()));
                }
                if (isTeacherBusyForUpdate(lecture)) {
                    throw new LectureExeption(String.format("Учитель уже занят на время %s",
                            lecture.getTime().getStartLecture()));
                }
                validateCommon(lecture, "обновить");
                break;
            default:
                return "Неизвестный контекст валидации: " + context;
        }
        return null;
    }

    private void validateCommon(Lecture lecture, String action) {
        if (!beginningAndEndLectureCorrect(lecture)) {
            throw new LectureExeption(String.format("Время лекции должно быть между %s и %s",
                    startLectionDay, endLectionDay));
        }
        if (lectureNotOnHoliday(lecture)) {
            throw new LectureExeption("Лекция не может быть назначена на праздничный день");
        }
        if (!isTeacherSubject(lecture)) {
            throw new LectureExeption("Учитель не ведёт указанный предмет");
        }
    }

    public Lecture create(Lecture lecture) {
        logger.debug("Creating lecture: {}", lecture);
        validate(lecture, ValidationContext.METHOD_CREATE);
        return lectureRepository.save(lecture);
    }

    public Lecture update(Lecture lecture) {
        logger.debug("Updating lecture: {}", lecture);
        validate(lecture, ValidationContext.METHOD_UPDATE);
        return lectureRepository.save(lecture);
    }
    @Transactional
    public void deleteById(Long id) {
        // Удаляем связи с группами т.к. у нас двухсторонняя связь c 2 List
        logger.debug("Deleting lecture with id: {}", id);
        Lecture lecture = lectureRepository.findByIdWithGroups(id)
                .orElseThrow(() -> new ResourceNotFoundException("Лекция не найдена"));

        lecture.getGroups().forEach(group -> Hibernate.initialize(group.getLectures()));
        lecture.getGroups().forEach(group -> group.getLectures().remove(lecture));
        lecture.getGroups().clear();

        lectureRepository.delete(lecture);
    }

    @Transactional(readOnly = true)
    public LectureDto findById(Long id) {
        // Чтобы lazy поля могли подгружаться сохраняем сессию, стандартный JPA не подходит
        return lectureRepository.findById(id)
                .map(lectureMapper::toDto)
                .orElseThrow(() -> new ResourceNotFoundException("Лекция не найдена"));
    }

    @Transactional(readOnly = true)
    public Optional<LectureDto> findEntityById(Long id) {
        return lectureRepository.findById(id)
                .map(lectureMapper::toDto);
    }

    public List<Lecture> findAll() {
        logger.debug("Find all lectures");
        return lectureRepository.findAll();
    }

    @Transactional(readOnly = true)
    public Page<LectureDto> findAllWithGroups(Pageable pageable) {
        // Пагинатор не может работать корректно с ManyToMany если там есть 2 List, требуется ручная разбивка
        Page<Lecture> page = lectureRepository.findAllLectures(pageable);

        List<Long> ids = page.getContent().stream()
                .map(Lecture::getId)
                .toList();

        Map<Long, Lecture> lectureMap = lectureRepository.findWithGroupsByIdIn(ids).stream()
                .collect(Collectors.toMap(Lecture::getId, Function.identity()));

        List<Lecture> mergedLectures = page.getContent().stream()
                .map(lecture -> lectureMap.getOrDefault(lecture.getId(), lecture))
                .toList();

        return new PageImpl<>(mergedLectures, pageable, page.getTotalElements())
                .map(lectureMapper::toDto);
    }

    public boolean isSingle(Lecture lecture) {
        logger.debug("Check audience is single");
        return lectureRepository.existsByTeacherIdAndSubjectIdAndTimeIdAndAudienceId(lecture.getTeacher().getId(),
                                                                                    lecture.getSubject().getId(),
                                                                                    lecture.getTime().getId(),
                                                                                    lecture.getAudience().getId());
    }

    public boolean existsById(Long id) {
        logger.debug("Check lecture is single");
        return lectureRepository.existsById(id);
    }

    private boolean beginningAndEndLectureCorrect(Lecture lecture) {
        logger.debug("Check startLectureLocal and endLectureLocal correct");
        LocalTime startLectureLocal = LocalTime.from(lecture.getTime().getStartLecture());
        LocalTime endLectureLocal = LocalTime.from(lecture.getTime().getEndLecture());

        return (startLectureLocal.isAfter(startLectionDay) || startLectureLocal.equals(startLectionDay)) &&
                (endLectureLocal.isBefore(endLectionDay) || endLectureLocal.equals(endLectionDay));
    }

    private boolean lectureNotOnHoliday(Lecture lecture) {
        logger.debug("Check lecture does not fall on the holiday");
        return holidayRepository.existsByDateInHoliday(lecture.getTime().getStartLecture().toLocalDate());
    }

    private boolean isTeacherSubject(Lecture lecture) {
        logger.debug("Check that the teacher has assigned a subject");
        return subjectRepository.existsByTeachers_IdAndId(lecture.getTeacher().getId(), lecture.getSubject().getId());
    }

    private boolean isAudienceFreeForCreate(Lecture lecture) {
        logger.debug("Check that the audience is free at this time for create");
        return lectureRepository.existsByTime_IdAndAudience_Id(lecture.getAudience().getId(), lecture.getTime().getId());
    }

    private boolean isAudienceFreeForUpdate(Lecture lecture) {
        logger.debug("Check that the audience is free at this time for update");
        return lectureRepository.findByAudienceDateAndLectureTimeForUpdate(lecture.getTime().getId(), lecture.getAudience().getId(), lecture.getId());
    }

    private boolean isTeacherBusyForCreate(Lecture lecture) {
        logger.debug("Check if the teacher is busy on the current date for create");
        List<Lecture> lectureList = lectureRepository.getTimetableTeacherForCreate(lecture.getTeacher().getId(),
                                                                                    lecture.getTime().getStartLecture().toLocalDate().atStartOfDay(),
                                                                                    lecture.getTime().getStartLecture().toLocalDate().atTime(LocalTime.MAX));

        return lectureList.stream().filter(lecture1 -> lecture1.getTime().getStartLecture().equals(lecture.getTime().getStartLecture())).findAny().isEmpty();
    }

    private boolean isTeacherBusyForUpdate(Lecture lecture) {
        if (lecture == null || lecture.getTime() == null || lecture.getTime().getId() == null) {
            throw new IllegalArgumentException("Lecture или время не может быть null");
        }

        List<Lecture> conflictingLectures = lectureRepository.getTimetableTeacherForUpdate(lecture.getTeacher().getId(), lecture.getTime().getId(), lecture.getId());

        return !conflictingLectures.isEmpty();
    }

    public List<Lecture> findByTeacherIdAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        return lectureRepository.findLecturesByTeacherAndPeriod(start.atStartOfDay(), end.atStartOfDay(), teacher.getId());
    }
}
