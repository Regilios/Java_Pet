package org.example.univer.services;

import org.example.univer.config.AppSettings;
import org.example.univer.dto.LectureDto;
import org.example.univer.exeption.*;
import org.example.univer.mappers.LectureMapper;
import org.example.univer.models.Group;
import org.example.univer.models.Lecture;
import org.example.univer.models.Teacher;
import org.example.univer.repositories.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
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
import java.util.stream.Collectors;

@Service
public class LectureService {
    private SubjectRepository subjectRepository;
    private HolidayRepository holidayRepository;

    private AppSettings appSettings;
    private LocalTime startLectionDay ;
    private LocalTime endLectionDay;

    private LectureRepository lectureRepository;
    private LectureMapper lectureMapper;


    @Autowired
    public LectureService(LectureMapper lectureMapper, LectureRepository lectureRepository, SubjectRepository subjectRepository, HolidayRepository holidayRepository, AppSettings appSettings) {
        this.subjectRepository = subjectRepository;
        this.holidayRepository = holidayRepository;
        this.lectureRepository = lectureRepository;
        this.lectureMapper = lectureMapper;
        if (appSettings.getStartLectionDay() != null && appSettings.getEndLectionDay() != null) {
            this.startLectionDay = LocalTime.parse(appSettings.getStartLectionDay(), DateTimeFormatter.ofPattern("HH:mm"));
            this.endLectionDay = LocalTime.parse(appSettings.getEndLectionDay(), DateTimeFormatter.ofPattern("HH:mm"));
        } else {
            throw new IllegalArgumentException("Start and end lection day must be set in configuration.");
        }
    }

    private static final Logger logger = LoggerFactory.getLogger(LectureService.class);

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Lecture lecture, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(lecture)) {
                    throw new InvalidParameterException("Невозможно создать лекцию! Лекция с такими параметрами уже существует!");
                }
                if (isAudienceFreeForCreate(lecture)) {
                    throw new LectureExeption("Невозможно создать лекцию! Аудитория: " + lecture.getAudience().getRoomNumber() + " уже занята на время: " + lecture.getTime().getStartLecture());
                }
                if (!isTeacherBusyForCreate(lecture)) {
                    throw new LectureExeption("Невозможно создать лекцию! У учителя уже назначена лекция на: " + lecture.getTime().getStartLecture());
                }
                validateCommon(lecture, "создать");
                break;
            case METHOD_UPDATE:
                if (isAudienceFreeForUpdate(lecture)) {
                    throw new LectureExeption("Невозможно обновить лекцию! Аудитория: " + lecture.getAudience().getRoomNumber() + " уже занята на время: " + lecture.getTime().getStartLecture());
                }
                if (isTeacherBusyForUpdate(lecture)) {
                    throw new LectureExeption("Невозможно обновить лекцию! У учителя уже назначена лекция на: " + lecture.getTime().getStartLecture());
                }
                validateCommon(lecture, "обновить");
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен " + context;
        }
        return null;
    }

    private void validateCommon(Lecture lecture, String action) {
        if (!beginningAndEndLectureCorrect(lecture)) {
            throw new LectureExeption("Невозможно " + action + " лекцию! Начало лекции не должно быть раньше: " + startLectionDay + " а конец лекции не позднее: " + endLectionDay);
        }
        if (lectureNotOnHoliday(lecture)) {
            throw new LectureExeption("Невозможно " + action + " лекцию! Лекция не может быть в праздник.");
        }
        if (!isTeacherSubject(lecture)) {
            throw new LectureExeption("Невозможно " + action + " лекцию! Учитель не ведёт заданный предмет.");
        }
    }

    public void create(Lecture lecture) {
        logger.debug("Start create Lecture");
        try {
            validate(lecture, ValidationContext.METHOD_CREATE);
            logger.debug("validate completed");
            lectureRepository.save(lecture);
            logger.debug("Lecture created");
        } catch (LectureExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта лекции: {}", e.getMessage(), e);
            throw new NullEntityException("Объект лекции не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта лекции: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта лекции", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект лекции не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта лекции", e);
        }
    }

    public void update(Lecture lecture) {
        logger.debug("Start update holiday");
        try {
            validate(lecture, ValidationContext.METHOD_UPDATE);
            lectureRepository.save(lecture);
            logger.debug("Lecture updated");
        } catch (LectureExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта лекции: {}", e.getMessage(), e);
            throw new NullEntityException("Объект лекции не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта лекции: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта лекции", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект лекции не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта лекции", e);
        }
    }
    @Transactional
    public void deleteById(Long id) {
        logger.debug("Delete lecture width id: {}", id);
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture not found"));

        // Удаляем связи с группами т.к. у нас двустороння связь
        for (Group group : lecture.getGroups()) {
            group.getLectures().remove(lecture);
        }
        lecture.getGroups().clear();

        lectureRepository.delete(lecture);
    }

    @Transactional(readOnly = true)
    // Чтобы lazy поля могли подгружаться сохраняем сессию, стандартный JPA не подходит
    public LectureDto findById(Long id) {
        Lecture lecture = lectureRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Lecture not found"));

        return lectureMapper.toDto(lecture);
    }

    public List<Lecture> findAll() {
        logger.debug("Find all lectures");
        return lectureRepository.findAll();
    }
    @Transactional(readOnly = true)
    public Page<LectureDto> findAllWithGroups(Pageable pageable) {
        // Пагинатор не может работать корректно с ManyToMany если там есть 2 List, требуется ручная разбивка
        Page<Lecture> page = lectureRepository.findAllLectures(pageable);

        // Подгружаем группы одной пачкой
        List<Long> ids = page.getContent().stream()
                .map(Lecture::getId)
                .collect(Collectors.toList());

        List<Lecture> withGroups = lectureRepository.findWithGroupsByIdIn(ids);

        // Map them by ID for fast access
        Map<Long, Lecture> lectureWithGroupsMap = withGroups.stream()
                .collect(Collectors.toMap(Lecture::getId, l -> l));

        // Объединяем результат
        List<Lecture> mergedLectures = page.getContent().stream()
                .map(l -> {
                    Lecture complete = lectureWithGroupsMap.get(l.getId());
                    return complete != null ? complete : l;
                })
                .collect(Collectors.toList());


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
