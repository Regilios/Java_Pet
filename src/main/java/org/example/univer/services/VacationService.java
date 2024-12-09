package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoVacationInterface;
import org.example.univer.models.Vacation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;

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
                    throw new IllegalArgumentException("Невозможно создать отпуск! Отпуск с такими параметрами уже существует!");
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
            throw new IllegalArgumentException("Невозможно " + action + " отпуск! Начало отпуска не может быть позднее конца отпуска");
        }
        if (!vacationMinAndMaxDayCorrect(vacation)) {
            throw new IllegalArgumentException("Невозможно " + action + " отпуск! Отпуск должен быть минимум:" + minVacationDay + " дней и максимум: " + maxVacationDay + "дней");
        }
    }

    public void create(Vacation vacation) {
        logger.debug("Start create vacation");
        try {
            validate(vacation, VacationService.ValidationContext.METHOD_CREATE);
            daoVacationInterface.create(vacation);
            logger.debug("Vacation created");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void update(Vacation vacation) {
        logger.debug("Start update vacation");
        try {
            validate(vacation, VacationService.ValidationContext.METHOD_UPDATE);
            daoVacationInterface.update(vacation);
            logger.debug("Vacation updated");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete vacation width id: {}", id);
        daoVacationInterface.deleteById(id);
    }

    public Vacation findById(Long id) {
        logger.debug("Find vacation width id: {}", id);
        return daoVacationInterface.findById(id);
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
        return vacation.getStartJobLocal().isBefore(vacation.getEndJobLocal());
    }

    private boolean vacationMinAndMaxDayCorrect(Vacation vacation) {
        long daysBetween = ChronoUnit.DAYS.between(vacation.getStartJobLocal(), vacation.getEndJobLocal());
        return Math.abs(daysBetween) <= maxVacationDay && Math.abs(daysBetween) >= minVacationDay;
    }
}
