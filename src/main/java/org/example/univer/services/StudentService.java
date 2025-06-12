package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.exeption.StudentExeption;
import org.example.univer.models.Student;
import org.example.univer.repositories.StudentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class StudentService {
    private final StudentRepository studentRepository;
    private static final Logger logger = LoggerFactory.getLogger(StudentService.class);
    private final AppSettings appSettings;
    private Integer maxGroupSize;


    @PostConstruct
    public void init() {
        this.maxGroupSize = appSettings.getMaxGroupSize();
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

    public Student create(Student student) {
        logger.debug("Creating student: {}", student);
        validate(student, ValidationContext.METHOD_CREATE);
        return studentRepository.save(student);
    }

    public Student update(Student student) {
        logger.debug("Updating student: {}", student);
        validate(student, ValidationContext.METHOD_UPDATE);
        return studentRepository.save(student);
    }

    private boolean checkGroupSize(Student student) {
        logger.debug("Checking that there is space in the group");
        return studentRepository.countByGroup_Id(student.getGroup().getId()) <= maxGroupSize;
    }

    public void deleteById(Long id) {
        logger.debug("Delete student width id: {}", id);
        studentRepository.deleteById(id);
    }

    public Optional<Student> findById(Long id) {
        logger.debug("Find student width id: {}", id);
        return studentRepository.findById(id);
    }

    public List<Student> findAll() {
        logger.debug("Find all students");
        return studentRepository.findAll();
    }

    public Page<Student> findAll(Pageable pageable) {
        logger.debug("Find all audiences paginated");
        return studentRepository.findAllByOrderById(pageable);
    }

    public boolean isSingle(Student student) {
        logger.debug("Checking student is single");
        return studentRepository.existsByFirstNameAndLastName(student.getFirstName(), student.getLastName());
    }

    public boolean existsById(Long id) {
        logger.debug("Checking student is single");
        return studentRepository.existsById(id);
    }
}
