package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoStudentInterface;
import org.example.univer.exeption.*;
import org.example.univer.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StudentService {
    private DaoStudentInterface daoStudentInterface;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);

    @Value("#{${maxGroupSize}}")
    private Integer maxGroupSize;

    @Autowired
    public StudentService(DaoStudentInterface daoStudentInterface) {
        this.daoStudentInterface = daoStudentInterface;
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Student student, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(student)) {
                    throw new InvalidParameterException("Невозможно создать студента! Студент с такими параметрами уже существует!");
                }
                validateCommon(student, "создать");
                break;
            case METHOD_UPDATE:
                validateCommon(student, "обновить");
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен " + context;
        }
        return null;
    }

    private void validateCommon(Student student, String action) {
        if (!checkGroupSize(student)) {
            throw new StudentExeption("Невозможно " + action + " студента! Группа заполнена!");
        }
    }

    public void create(Student student) {
        logger.debug("Start create student");
        try {
            validate(student, ValidationContext.METHOD_CREATE);
            daoStudentInterface.create(student);
            logger.debug("Student created");
        } catch (StudentExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта студента: {}", e.getMessage(), e);
            throw new NullEntityException("Объект студента не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта студента: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта студента", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект студента не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта студента", e);
        }
    }

    public void update(Student student) {
        logger.debug("Start update student");
        try {
            validate(student, ValidationContext.METHOD_UPDATE);
            daoStudentInterface.update(student);
            logger.debug("Student updated");
        } catch (StudentExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта студента: {}", e.getMessage(), e);
            throw new NullEntityException("Объект студента не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта студента: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта студента", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект студента не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта студента", e);
        }
    }

    private boolean checkGroupSize(Student student) {
        logger.debug("Сheck that there is space in the group");
        return daoStudentInterface.checkGroupSize(student) <= maxGroupSize;
    }

    public void deleteById(Long id) {
        logger.debug("Delete student width id: {}", id);
        daoStudentInterface.deleteById(id);
    }

    public Student findById(Long id) {
        logger.debug("Find student width id: {}", id);
        return daoStudentInterface.findById(id);
    }

    public List<Student> findAll() {
        logger.debug("Find all students");
        return daoStudentInterface.findAll();
    }

    public Page<Student> findAll(Pageable pageable) {
        logger.debug("Find all audiences paginated");
        return daoStudentInterface.findPaginatedStudents(pageable);
    }

    public boolean isSingle(Student student) {
        logger.debug("Check student is single");
        return daoStudentInterface.isSingle(student);
    }
}
