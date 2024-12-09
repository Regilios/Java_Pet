package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoLectureTimeInterface;
import org.example.univer.models.LectureTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;

@Service
public class LectureTimeService {
    DaoLectureTimeInterface daoLectureTimeInterface;
    private static final Logger logger = LoggerFactory.getLogger(LectureTimeService.class);

    @Value("#{${minimumLectureTimeMinutes}}")
    private Integer minimumLectureTimeMinutes;

    @Autowired
    public LectureTimeService(DaoLectureTimeInterface daoLectureTimeInterface) {
        this.daoLectureTimeInterface = daoLectureTimeInterface;
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }
    public String validate(LectureTime lectureTime, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(lectureTime)) {
                    throw new IllegalArgumentException("Невозможно создать время для лекции! Время с: " + lectureTime.getStart() + " по " + lectureTime.getEnd() + "уже существует!");
                }
                validateCommon(lectureTime, "создать");
                break;
            case METHOD_UPDATE:
                validateCommon(lectureTime, "обновить");
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен " + context;
        }
        return null;
    }

    private void validateCommon(LectureTime lectureTime, String action) {
        if (!isTimeLectionCorrect(lectureTime)) {
            throw new IllegalArgumentException("Невозможно " + action + " время для лекции! Начало лекции не может быть позднее её окончания!");
        }
        if (!timeLectionIsNotLessAssignedTime(lectureTime)) {
            throw new IllegalArgumentException("Невозможно " + action +  "время для лекции! Временой отрезок не может быть меньше: " + minimumLectureTimeMinutes);
        }
    }


    public void create(LectureTime lectureTime) {
        logger.debug("Start create LectionTime");
        try {
            validate(lectureTime, ValidationContext.METHOD_CREATE);
            daoLectureTimeInterface.create(lectureTime);
            logger.debug("LectionTime created");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void update(LectureTime lectureTime) {
        logger.debug("Start update LectionTime");
        try {
            validate(lectureTime, ValidationContext.METHOD_UPDATE);
            daoLectureTimeInterface.update(lectureTime);
            logger.debug("LectionTime updated");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete lectionTime width id: {}", id);
        daoLectureTimeInterface.deleteById(id);
    }

    public LectureTime findById(Long id) {
        logger.debug("Find lectionTime width id: {}", id);
        return daoLectureTimeInterface.findById(id);
    }

    public List<LectureTime> findAll() {
        logger.debug("Find all lectionTime");
        return daoLectureTimeInterface.findAll();
    }

    public boolean isSingle(LectureTime lectureTime) {
        logger.debug("Check lectionTime is single");
        return daoLectureTimeInterface.isSingle(lectureTime);
    }

    public boolean isTimeLectionCorrect(LectureTime lectureTime) {
        return lectureTime.getStartLocal().isBefore(lectureTime.getEndLocal());
    }

    public boolean timeLectionIsNotLessAssignedTime(LectureTime lectureTime) {
        return Duration.between(lectureTime.getStartLocal(), lectureTime.getEndLocal()).abs().toMinutes() >= minimumLectureTimeMinutes;
    }
}
