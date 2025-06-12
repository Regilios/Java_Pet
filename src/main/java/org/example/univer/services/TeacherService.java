package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.exeption.TeacherExeption;
import org.example.univer.models.Teacher;
import org.example.univer.repositories.TeacherRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TeacherService {
    private final TeacherRepository teacherRepository;
    private final SubjectService subjectService;
    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);
    private final AppSettings appSettings;
    private String genderTeacher;

    @PostConstruct
    public void init() {
        this.genderTeacher = appSettings.getGenderTeacher();
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Teacher teacher, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(teacher)) {
                    throw new InvalidParameterException("Невозможно создать учителя! Учитель с такими параметрами уже существует!");
                }
                validateCommon(teacher, "создать");
                break;
            case METHOD_UPDATE:
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен " + context;
        }
        return null;
    }

    private void validateCommon(Teacher teacher, String action) {
        if (!checkGender(teacher)) {
            throw new TeacherExeption("Невозможно " + action + " учителя! Пол учителя должен быть: " + genderTeacher);
        }
    }

    public Teacher create(Teacher teacher) {
        logger.debug("Creating teacher: {}", teacher);
        validate(teacher, ValidationContext.METHOD_CREATE);
        return teacherRepository.save(teacher);
    }

    public Teacher update(Teacher teacher) {
        logger.debug("Updating teacher: {}", teacher);
        validate(teacher, ValidationContext.METHOD_UPDATE);
        return teacherRepository.save(teacher);
    }

    public void deleteById(Long id) {
        logger.debug("Delete teacher width id: {}", id);
        teacherRepository.deleteById(id);
    }

    public Optional<Teacher> findById(Long id) {
        logger.debug("Find teacher width id: {}", id);
        return teacherRepository.findById(id);
    }

    public List<Teacher> findAll() {
        logger.debug("Find all teachers");
        return teacherRepository.findAll();
    }

    public boolean isSingle(Teacher teacher) {
        logger.debug("Check teacher is single");
        return teacherRepository.existsByFirstNameAndLastName(teacher.getFirstName(),teacher.getLastName());
    }

    public boolean existsById(Long id) {
        logger.debug("Check teacher is single");
        return teacherRepository.existsById(id);
    }

    private boolean checkGender(Teacher teacher) {
        return teacher.getGender().toString().equals(genderTeacher);
    }
}
