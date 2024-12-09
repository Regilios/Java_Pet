package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoSubjectInterface;
import org.example.univer.models.Subject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SubjectService {
    private DaoSubjectInterface daoSubjectInterface;
    private static final Logger logger = LoggerFactory.getLogger(SubjectService.class);

    @Value("#{${minSizeDescription}}")
    private Integer minSizeDescription;

    @Autowired
    public SubjectService(DaoSubjectInterface daoSubjectInterface) {
        this.daoSubjectInterface = daoSubjectInterface;
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Subject subject, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(subject)) {
                    throw new IllegalArgumentException("Невозможно создать передмет! Предмет с такими параметрами уже существует!");
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
            throw new IllegalArgumentException("Невозможно " + action + " предмет! Описание не заполнено и не может быть меньше чем: "+minSizeDescription+" символов!");
        }
    }

    public void create(Subject subject) {
        logger.debug("Start create subject");
        try {
            validate(subject, SubjectService.ValidationContext.METHOD_CREATE);
            daoSubjectInterface.create(subject);
            logger.debug("Subject created");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void update(Subject subject) {
        logger.debug("Start update subject");
        try {
            validate(subject, SubjectService.ValidationContext.METHOD_UPDATE);
            daoSubjectInterface.update(subject);
            logger.debug("Subject updated");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }
    public void deleteById(Long id) {
        logger.debug("Delete subject width id: {}", id);
        daoSubjectInterface.deleteById(id);
    }

    public Subject findById(Long id) {
        logger.debug("Find subject width id: {}", id);
        return daoSubjectInterface.findById(id);
    }

    public List<Subject> findAll() {
        logger.debug("Find all subjects");
        return daoSubjectInterface.findAll();
    }

    public boolean isSingle(Subject subject) {
        logger.debug("Check subject is single");
        return daoSubjectInterface.isSingle(subject);
    }

    private boolean descriptionNotEmpty(Subject subject) {
        return subject.getDescription().length() >= minSizeDescription;
    }
}
