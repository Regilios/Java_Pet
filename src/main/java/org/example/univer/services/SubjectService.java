package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.exeption.SubjectExeption;
import org.example.univer.models.Subject;
import org.example.univer.repositories.SubjectRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SubjectService {
    private final SubjectRepository subjectRepository;
    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);
    private final AppSettings appSettings;
    private Integer minSizeDescription;

    @PostConstruct
    public void init() {
        this.minSizeDescription = appSettings.getMinSizeDescription();
    }
    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Subject subject, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(subject)) {
                    throw new InvalidParameterException("Невозможно создать предмет! Предмет с такими параметрами уже существует!");
                }
                validateCommon(subject, "создать");
                break;
            case METHOD_UPDATE:
                validateCommon(subject, "обновить");
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен " + context;
        }
        return null;
    }

    private void validateCommon(Subject subject, String action) {
        if (!descriptionNotEmpty(subject)) {
            throw new SubjectExeption("Невозможно " + action + " предмет! Описание не заполнено и не может быть меньше чем: " + minSizeDescription + " символов!");
        }
    }

    public Subject create(Subject subject) {
        logger.debug("Creating subject: {}", subject);
         validate(subject, SubjectService.ValidationContext.METHOD_CREATE);
         return subjectRepository.save(subject);
    }

    public Subject update(Subject subject) {
        logger.debug("Updating subject: {}", subject);
        validate(subject, SubjectService.ValidationContext.METHOD_UPDATE);
        return subjectRepository.save(subject);
    }

    public void deleteById(Long id) {
        logger.debug("Delete subject width id: {}", id);
        subjectRepository.deleteById(id);
    }

    public Optional<Subject> findById(Long id) {
        logger.debug("Find subject width id: {}", id);
        return subjectRepository.findById(id);
    }

    public List<Subject> findAll() {
        logger.debug("Find all subjects");
        return subjectRepository.findAll();
    }

    public boolean isSingle(Subject subject) {
        logger.debug("Check subject is single");
        return subjectRepository.existsByName(subject.getName());
    }

    public boolean existsById(Long id) {
        logger.debug("Check subject is single");
        return subjectRepository.existsById(id);
    }

    public List<Subject> getSubjectById(Long teacher_id) {
        logger.debug("Get list subject by id");
        return subjectRepository.findByTeachers_Id(teacher_id);
    }

    private boolean descriptionNotEmpty(Subject subject) {
        return subject.getDescription().length() >= minSizeDescription;
    }
}
