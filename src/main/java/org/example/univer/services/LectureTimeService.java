package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.exeption.LectureTimeExeption;
import org.example.univer.models.LectureTime;
import org.example.univer.repositories.LectureTimeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class LectureTimeService {
    private final LectureTimeRepository lectureTimeRepository;
    private static final Logger logger = LoggerFactory.getLogger(LectureTimeService.class);
    private final AppSettings appSettings;
    private Integer minimumLectureTimeMinutes;

    @PostConstruct
    public void init() {
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
                    throw new InvalidParameterException(String.format("Невозможно создать время для лекции! Время с: %s по %s уже существует!", lectureTime.getStartLecture(), lectureTime.getEndLecture()));
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


    public LectureTime create(LectureTime lectureTime) {
        logger.debug("Creating lectureTime: {}", lectureTime);
        validate(lectureTime, ValidationContext.METHOD_CREATE);
        return lectureTimeRepository.save(lectureTime);
    }

    public LectureTime update(LectureTime lectureTime) {
        logger.debug("Updating lectureTime: {}", lectureTime);
        validate(lectureTime, ValidationContext.METHOD_UPDATE);
        return lectureTimeRepository.save(lectureTime);
    }

    public void deleteById(Long id) {
        logger.debug("Delete lectureTime width id: {}", id);
        lectureTimeRepository.deleteById(id);
    }

    public Optional<LectureTime> findById(Long id) {
        logger.debug("Find lectureTime width id: {}", id);
        return lectureTimeRepository.findById(id);
    }

    public List<LectureTime> findAll() {
        logger.debug("Find all lectureTime");
        return lectureTimeRepository.findAll();
    }

    public boolean isSingle(LectureTime lectureTime) {
        logger.debug("Check lectureTime is single");
        Long count = lectureTimeRepository.countByStartLectureAndEndLecture(lectureTime.getStartLecture(), lectureTime.getEndLecture());
        return count > 0;
    }

    public boolean existsById(Long id) {
        logger.debug("Check lectureTime is single");
        return lectureTimeRepository.existsById(id);
    }

    public boolean isTimeLectionCorrect(LectureTime lectureTime) {
        return lectureTime.getStartLecture().isBefore(lectureTime.getEndLecture());
    }

    public boolean timeLectionIsNotLessAssignedTime(LectureTime lectureTime) {
        return Duration.between(lectureTime.getStartLecture(), lectureTime.getEndLecture()).abs().toMinutes() >= minimumLectureTimeMinutes;
    }
}
