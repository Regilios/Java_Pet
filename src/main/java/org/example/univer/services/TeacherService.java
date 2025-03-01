package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoTeacherInterface;
import org.example.univer.exeption.*;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class TeacherService {
    private DaoTeacherInterface daoTeacherInterface;
    private SubjectService subjectService;
    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    @Value("#{${genderTeacher:FEMALE}}")
    private String genderTeacher;

    @Autowired
    public TeacherService(DaoTeacherInterface daoTeacherInterface, SubjectService subjectService) {
        this.daoTeacherInterface = daoTeacherInterface;
        this.subjectService = subjectService;
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

    public void create(Teacher teacher) {
        logger.debug("Start create teacher");
        try {
            validate(teacher, ValidationContext.METHOD_CREATE);
            daoTeacherInterface.create(teacher);
            logger.debug("Teacher created");
        } catch (TeacherExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта учитель: {}", e.getMessage(), e);
            throw new NullEntityException("Объект учитель не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта учитель: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта учитель", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект учитель не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта учитель", e);
        }
    }

    public void update(Teacher teacher) {
        logger.debug("Start update teacher");
        try {
            validate(teacher, ValidationContext.METHOD_UPDATE);
            Teacher teacherOld = findById(teacher.getId());
            List<Subject> subjects = daoTeacherInterface.getListSubjectForTeacher(teacherOld.getId()).stream()
                    .map(subjectService::findById)
                    .collect(Collectors.toList());
            teacherOld.setSubject(subjects);
            daoTeacherInterface.update(teacher, teacherOld);
            logger.debug("Teacher updated");
        } catch (TeacherExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта учитель: {}", e.getMessage(), e);
            throw new NullEntityException("Объект учитель не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта учитель: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта учитель", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект учитель не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта учитель", e);
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete teacher width id: {}", id);
        daoTeacherInterface.deleteById(id);
    }

    public Teacher findById(Long id) {
        logger.debug("Find teacher width id: {}", id);
        return daoTeacherInterface.findById(id);
    }

    public List<Teacher> findAll() {
        logger.debug("Find all teachers");
        return daoTeacherInterface.findAll();
    }

    public boolean isSingle(Teacher teacher) {
        logger.debug("Check teacher is single");
        return daoTeacherInterface.isSingle(teacher);
    }

    public List<Long> getListSubjectForTeacher(Long teacherId) {
        logger.debug("Get list subject by id teacher");
        return daoTeacherInterface.getListSubjectForTeacher(teacherId);
    }
    private boolean checkGender(Teacher teacher) {
        return teacher.getGender().toString().equals(genderTeacher);
    }
}
