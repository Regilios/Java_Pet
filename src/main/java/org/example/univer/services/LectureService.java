package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoHolidayInterface;
import org.example.univer.dao.interfaces.DaoLectureInterface;
import org.example.univer.dao.interfaces.DaoSubjectInterface;
import org.example.univer.models.Lecture;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class LectureService {
    private DaoLectureInterface daoLectureInterface;
    private DaoHolidayInterface daoHolidayInterface;
    private DaoSubjectInterface daoSubjectInterface;

    @Value("#{${startLectionDay}}")
    private String startLection;
    private LocalTime startLectionDay;

    @Value("#{${endLectionDay}}")
    private String endLection;
    private LocalTime endLectionDay;

    @PostConstruct
    public void init() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm");
        this.startLectionDay = LocalTime.parse(startLection, formatter);
        this.endLectionDay = LocalTime.parse(endLection, formatter);
    }

    @Autowired
    public LectureService(DaoLectureInterface daoLectureInterface, DaoHolidayInterface daoHolidayInterface, DaoSubjectInterface daoSubjectInterface) {
        this.daoLectureInterface = daoLectureInterface;
        this.daoHolidayInterface = daoHolidayInterface;
        this.daoSubjectInterface = daoSubjectInterface;
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
                    throw new IllegalArgumentException("Невозможно создать лекцию! Лекция с такими параметрами уже существует!");
                }
                validateCommon(lecture, "создать");
                break;
            case METHOD_UPDATE:
                validateCommon(lecture, "обновить");
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен " + context;
        }
        return null;
    }

    private void validateCommon(Lecture lecture, String action) {
        if (!beginningAndEndLectureCorrect(lecture)) {
            throw new IllegalArgumentException("Невозможно " + action + " лекцию! Начало лекции не должно быть раньше: " + startLectionDay + " а конец лекции не позднее: " + endLectionDay);
        }
        if (lectureNotOnHoliday(lecture)) {
            throw new IllegalArgumentException("Невозможно " + action + " лекцию! Лекция не может быть в праздник.");
        }
        if (!isTeacherSubject(lecture)) {
            throw new IllegalArgumentException("Невозможно " + action + " лекцию! Учитель не ведёт заданный предмет.");
        }
        if (isAudienceFree(lecture)) {
            throw new IllegalArgumentException("Невозможно " + action + " лекцию! Аудитория: " + lecture.getAudience().getRoomString() + " уже занята на время: " + lecture.getTime().getStart());
        }
        if (!isTeacherBusy(lecture)) {
            throw new IllegalArgumentException("Невозможно " + action + " лекцию! У учителя уже назначена лекция на: " + lecture.getTime().getStart());
        }
    }

    public void create(Lecture lecture) {
        logger.debug("Start create Lecture");
        try {
            validate(lecture, ValidationContext.METHOD_CREATE);
            daoLectureInterface.create(lecture);
            logger.debug("Lecture created");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void update(Lecture lecture) {
        logger.debug("Start update holiday");
        try {
            validate(lecture, ValidationContext.METHOD_UPDATE);
            daoLectureInterface.update(lecture);
            logger.debug("Lecture updated");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete lecture width id: {}", id);
        daoLectureInterface.deleteById(id);
    }

    public Lecture findById(Long id) {
        logger.debug("Find audience width id: {}", id);
        return daoLectureInterface.findById(id);
    }

    public List<Lecture> findAll() {
        logger.debug("Find all audiences");
        return daoLectureInterface.findAll();
    }

    public boolean isSingle(Lecture lecture) {
        logger.debug("Check audience is single");
        return daoLectureInterface.isSingle(lecture);
    }

    private boolean beginningAndEndLectureCorrect(Lecture lecture) {
        logger.debug("Check startLectionLocal and endLectionLocal correct");
        LocalTime startLectionLocal = LocalTime.from(lecture.getLocalTimeStart());
        LocalTime endLectionLocal = LocalTime.from(lecture.getLocalTimeEnd());

        return (startLectionLocal.isAfter(startLectionDay) || startLectionLocal.equals(startLectionDay)) &&
                (endLectionLocal.isBefore(endLectionDay) || endLectionLocal.equals(endLectionDay));
    }

    private boolean lectureNotOnHoliday(Lecture lecture) {
        logger.debug("Check lecture does not fall on the holiday");
        return daoHolidayInterface.lectureDoesNotFallOnHoliday(lecture.getTime().getStartLocal());
    }

    private boolean isTeacherSubject(Lecture lecture) {
        logger.debug("Check that the teacher has assigned a subject");
        return daoSubjectInterface.checkTeacherAssignedSubject(lecture.getTeacher(), lecture.getSubject());
    }

    private boolean isAudienceFree(Lecture lecture) {
        logger.debug("Сheck that the audience is free at this time");
        return daoLectureInterface.findByAudienceDateAndLectureTime(lecture.getAudience(), lecture.getTime());
    }

    private boolean isTeacherBusy(Lecture lecture) {
        logger.debug("Check if the teacher is busy on the current date");
        List<Lecture> lectureList = daoLectureInterface.getTimetableTeacher(lecture.getTeacher(), LocalDate.from(lecture.getTime().getStartLocal()));
        return lectureList.stream().filter(lecture1 -> lecture1.getTime().getStartLocal().equals(lecture.getTime().getStartLocal())).findAny().isEmpty();
    }
}
