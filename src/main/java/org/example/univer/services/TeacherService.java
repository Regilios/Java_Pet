package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoTeacherInterface;
import org.example.univer.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TeacherService {
    private DaoTeacherInterface daoTeacherInterface;
    private static final Logger logger = LoggerFactory.getLogger(TeacherService.class);

    @Value("#{${genderTeacher:FEMALE}}")
    private String genderTeacher;

    @Autowired
    public TeacherService(DaoTeacherInterface daoTeacherInterface) {
        this.daoTeacherInterface = daoTeacherInterface;
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Teacher teacher, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(teacher)) {
                    throw new IllegalArgumentException("Невозможно создать учителя! Учитель с такими параметрами уже существует!");
                }
                validateCommon(teacher, "создать");
                break;
            case METHOD_UPDATE:
                validateCommon(teacher, "обновить");
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен " + context;
        }
        return null;
    }

    private void validateCommon(Teacher teacher, String action) {
        if (!checkGender(teacher)) {
            throw new IllegalArgumentException("Невозможно " + action + " учителя! Пол учителя должен быть: " + genderTeacher);
        }
    }

    public void create(Teacher teacher) {
        logger.debug("Start create teacher");
        try {
            validate(teacher, ValidationContext.METHOD_CREATE);
            daoTeacherInterface.create(teacher);
            logger.debug("Teacher created");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void update(Teacher teacher) {
        logger.debug("Start update teacher");
        try {
            validate(teacher, ValidationContext.METHOD_UPDATE);
            daoTeacherInterface.update(teacher);
            logger.debug("Teacher updated");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
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

    private boolean checkGender(Teacher teacher) {
        return teacher.getGender().equals(genderTeacher);
    }
}
