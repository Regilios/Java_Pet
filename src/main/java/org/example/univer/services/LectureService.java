package org.example.univer.services;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoHolidayInterface;
import org.example.univer.dao.interfaces.DaoLectureInterface;
import org.example.univer.dao.interfaces.DaoSubjectInterface;
import org.example.univer.exeption.*;
import org.example.univer.models.Lecture;
import org.example.univer.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.Optional;

@Service
public class LectureService {
    private DaoLectureInterface daoLectureInterface;
    private DaoHolidayInterface daoHolidayInterface;
    private DaoSubjectInterface daoSubjectInterface;
    private GroupService groupService;
    private AppSettings appSettings;
    private LocalTime startLectionDay ;
    private LocalTime endLectionDay;

    @Autowired
    public LectureService(DaoLectureInterface daoLectureInterface, DaoHolidayInterface daoHolidayInterface, DaoSubjectInterface daoSubjectInterface, GroupService groupService, AppSettings appSettings) {
        this.daoLectureInterface = daoLectureInterface;
        this.daoHolidayInterface = daoHolidayInterface;
        this.daoSubjectInterface = daoSubjectInterface;
        this.groupService = groupService;

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
            daoLectureInterface.create(lecture);
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
            daoLectureInterface.update(lecture);
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

    public void deleteById(Long id) {
        logger.debug("Delete lecture width id: {}", id);
        daoLectureInterface.deleteById(id);
    }

    public Optional<Lecture> findById(Long id) {
        logger.debug("Find lecture width id: {}", id);
        return daoLectureInterface.findById(id);
    }

    public List<Lecture> findAll() {
        logger.debug("Find all lectures");
        return daoLectureInterface.findAll();
    }

    public Page<Lecture> findAllWithGroup(Pageable pageable) {
        logger.debug("Find all lectures with groups");
        return  daoLectureInterface.findPaginatedLecture(pageable);
    }

    public boolean isSingle(Lecture lecture) {
        logger.debug("Check audience is single");
        return daoLectureInterface.isSingle(lecture);
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
        return daoHolidayInterface.lectureDoesNotFallOnHoliday(lecture.getTime().getStartLecture());
    }

    private boolean isTeacherSubject(Lecture lecture) {
        logger.debug("Check that the teacher has assigned a subject");
        return daoSubjectInterface.checkTeacherAssignedSubject(lecture.getTeacher(), lecture.getSubject());
    }

    private boolean isAudienceFreeForCreate(Lecture lecture) {
        logger.debug("Сheck that the audience is free at this time for create");
        return daoLectureInterface.findByAudienceDateAndLectureTimeForCreate(lecture.getAudience(), lecture.getTime());
    }

    private boolean isAudienceFreeForUpdate(Lecture lecture) {
        logger.debug("Сheck that the audience is free at this time for update");
        return daoLectureInterface.findByAudienceDateAndLectureTimeForUpdate(lecture.getAudience(), lecture.getTime(), lecture.getId());
    }

    private boolean isTeacherBusyForCreate(Lecture lecture) {
        logger.debug("Check if the teacher is busy on the current date for create");
        List<Lecture> lectureList = daoLectureInterface.getTimetableTeacherForCreate(lecture.getTeacher(), LocalDate.from(lecture.getTime().getStartLecture()));
        return lectureList.stream().filter(lecture1 -> lecture1.getTime().getStartLecture().equals(lecture.getTime().getStartLecture())).findAny().isEmpty();
    }

    private boolean isTeacherBusyForUpdate(Lecture lecture) {
        if (lecture == null || lecture.getTime() == null || lecture.getTime().getId() == null) {
            throw new IllegalArgumentException("Lecture или время не может быть null");
        }

        List<Lecture> conflictingLectures = daoLectureInterface.getTimetableTeacherForUpdate(lecture.getTeacher(), lecture);

        return !conflictingLectures.isEmpty();
    }

    public List<Lecture> findByTeacherIdAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        return daoLectureInterface.findLecturesByTeacherAndPeriod(teacher, start, end);
    }
}
