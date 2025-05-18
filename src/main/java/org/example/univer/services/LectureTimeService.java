package org.example.univer.services;

import org.example.univer.config.AppSettings;
import org.example.univer.dao.interfaces.DaoLectureTimeInterface;
import org.example.univer.exeption.*;
import org.example.univer.models.LectureTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
public class LectureTimeService {
    DaoLectureTimeInterface daoLectureTimeInterface;
    private static final Logger logger = LoggerFactory.getLogger(LectureTimeService.class);
    private AppSettings appSettings;
    private Integer minimumLectureTimeMinutes;

    @Autowired
    public LectureTimeService(DaoLectureTimeInterface daoLectureTimeInterface, AppSettings appSettings) {
        this.daoLectureTimeInterface = daoLectureTimeInterface;
        this.appSettings = appSettings;
        this.minimumLectureTimeMinutes = appSettings.getMinimumLectureTimeMinutes();
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }
    public String validate(LectureTime lectureTime, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(lectureTime)) {
                    throw new InvalidParameterException("Невозможно создать время для лекции! Время с: " + lectureTime.getStartLecture() + " по " + lectureTime.getEndLecture() + "уже существует!");
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
            throw new LectureTimeExeption("Невозможно " + action + " время для лекции! Начало лекции не может быть позднее её окончания!");
        }
        if (!timeLectionIsNotLessAssignedTime(lectureTime)) {
            throw new LectureTimeExeption("Невозможно " + action +  "время для лекции! Временой отрезок не может быть меньше: " + minimumLectureTimeMinutes);
        }
    }


    public void create(LectureTime lectureTime) {
        logger.debug("Start create LectionTime");
        try {
            validate(lectureTime, ValidationContext.METHOD_CREATE);
            daoLectureTimeInterface.create(lectureTime);
            logger.debug("LectionTime created");
        } catch (LectureTimeExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта времени-лекции: {}", e.getMessage(), e);
            throw new NullEntityException("Объект времени-лекции не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта времени-лекции: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта времени-лекции", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект времени-лекции не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта времени-лекции", e);
        }
    }

    public void update(LectureTime lectureTime) {
        logger.debug("Start update LectionTime");
        try {
            validate(lectureTime, ValidationContext.METHOD_UPDATE);
            daoLectureTimeInterface.update(lectureTime);
            logger.debug("LectionTime updated");
        } catch (LectureTimeExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта времени-лекции: {}", e.getMessage(), e);
            throw new NullEntityException("Объект времени-лекции не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта времени-лекции: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта времени-лекции", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект времени-лекции не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта времени-лекции", e);
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete lectionTime width id: {}", id);
        daoLectureTimeInterface.deleteById(id);
    }

    public Optional<LectureTime> findById(Long id) {
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
        return lectureTime.getStartLecture().isBefore(lectureTime.getEndLecture());
    }

    public boolean timeLectionIsNotLessAssignedTime(LectureTime lectureTime) {
        return Duration.between(lectureTime.getStartLecture(), lectureTime.getEndLecture()).abs().toMinutes() >= minimumLectureTimeMinutes;
    }
}
