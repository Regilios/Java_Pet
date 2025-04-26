package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoVacationInterface;
import org.example.univer.exeption.*;
import org.example.univer.models.Vacation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class VacationService {
    private DaoVacationInterface daoVacationInterface;
    private static final Logger logger = LoggerFactory.getLogger(VacationService.class);

    @Value("#{${minVacationDay:7}}")
    private Integer minVacationDay;

    @Value("#{${maxVacationDay:30}}")
    private Integer maxVacationDay;

    @Autowired
    public VacationService(DaoVacationInterface daoVacationInterface) {
        this.daoVacationInterface = daoVacationInterface;
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Vacation vacation, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(vacation)) {
                    throw new InvalidParameterException("Невозможно создать отпуск! Отпуск с такими параметрами уже существует!");
                }
                validateCommon(vacation, "создать");
                break;
            case METHOD_UPDATE:
                validateCommon(vacation, "обновить");
                break;
            default:
                return "Контекст валидации отсутствует или неизвестен " + context;
        }
        return null;
    }

    private void validateCommon(Vacation vacation, String action) {
        if (!dataVacationCorrect(vacation)) {
            throw new VacationExeption("Невозможно " + action + " отпуск! Начало отпуска не может быть позднее конца отпуска");
        }
        if (!vacationMinAndMaxDayCorrect(vacation)) {
            throw new VacationExeption("Невозможно " + action + " отпуск! Отпуск должен быть минимум:" + minVacationDay + " дней и максимум: " + maxVacationDay + "дней");
        }
    }

    public void create(Vacation vacation) {
        logger.debug("Start create vacation");
        try {
            validate(vacation, VacationService.ValidationContext.METHOD_CREATE);
            daoVacationInterface.create(vacation);
            logger.debug("Vacation created");
        } catch (VacationExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта вакансии: {}", e.getMessage(), e);
            throw new NullEntityException("Объект вакансии не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта вакансии: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта вакансии", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект вакансии не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта вакансии", e);
        }
    }

    public void update(Vacation vacation) {
        logger.debug("Start update vacation");
        try {
            validate(vacation, VacationService.ValidationContext.METHOD_UPDATE);
            daoVacationInterface.update(vacation);
            logger.debug("Vacation updated");
        } catch (VacationExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта вакансии: {}", e.getMessage(), e);
            throw new NullEntityException("Объект вакансии не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта вакансии: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта вакансии", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект вакансии не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта вакансии", e);
        }
    }

    public void deleteById(Vacation vacation) {
        logger.debug("Delete vacation width id: {}", vacation.getId());
        daoVacationInterface.deleteById(vacation);
    }

    public Optional<Vacation> findById(Long id) {
        logger.debug("Find vacation width id: {}", id);
        return daoVacationInterface.findById(id);
    }

    public List<Vacation> findByTeacherId(Long id) {
        logger.debug("Find vacation width id: {}", id);
        return daoVacationInterface.findByTeacherId(id);
    }
    public List<Vacation> findAll() {
        logger.debug("Find all vacations");
        return daoVacationInterface.findAll();
    }

    public boolean isSingle(Vacation subject) {
        logger.debug("Check vacation is single");
        return daoVacationInterface.isSingle(subject);
    }

    private boolean dataVacationCorrect(Vacation vacation) {
        return vacation.getStartJob().isBefore(vacation.getEndJob());
    }

    private boolean vacationMinAndMaxDayCorrect(Vacation vacation) {
        long daysBetween = ChronoUnit.DAYS.between(vacation.getStartJob(), vacation.getEndJob());
        return Math.abs(daysBetween) <= maxVacationDay && Math.abs(daysBetween) >= minVacationDay;
    }
}
