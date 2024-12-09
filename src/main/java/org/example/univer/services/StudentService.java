package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoStudentInterface;
import org.example.univer.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
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
                    throw new IllegalArgumentException("Невозможно создать студента! Студент с такими параметрами уже существует!");
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
            throw new IllegalArgumentException("Невозможно " + action + " студента! Группа заполнена!");
        }
    }

    public void create(Student student) {
        logger.debug("Start create student");
        try {
            validate(student, ValidationContext.METHOD_CREATE);
            daoStudentInterface.create(student);
            logger.debug("Student created");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void update(Student student) {
        logger.debug("Start update student");
        try {
            validate(student, ValidationContext.METHOD_UPDATE);
            daoStudentInterface.update(student);
            logger.debug("Student updated");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
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

    public boolean isSingle(Student student) {
        logger.debug("Check student is single");
        return daoStudentInterface.isSingle(student);
    }
}
