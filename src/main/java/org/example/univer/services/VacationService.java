package org.example.univer.services;

import org.example.univer.config.AppSettings;
import org.example.univer.repositories.VacationRepository;
import org.example.univer.exeption.*;
import org.example.univer.models.Vacation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class VacationService {
    private VacationRepository vacationRepository;
    private static final Logger logger = LoggerFactory.getLogger(VacationService.class);
    private AppSettings appSettings;
    private Integer minVacationDay;
    private Integer maxVacationDay;

    @Autowired
    public VacationService(VacationRepository vacationRepository, AppSettings appSettings) {
        this.vacationRepository = vacationRepository;
        this.appSettings = appSettings;
        this.minVacationDay = appSettings.getMinVacationDay();
        this.maxVacationDay = appSettings.getMaxVacationDay();
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
            vacationRepository.save(vacation);
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
            vacationRepository.save(vacation);
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

    public void deleteById(Long id) {
        logger.debug("Delete vacation width id: {}", id);
        vacationRepository.deleteVacationById(id);
    }

    public Optional<Vacation> findById(Long id) {
        logger.debug("Find vacation width id: {}", id);
        return vacationRepository.findByIdWithTeacherCathedraAndSubjects(id);
    }

    public List<Vacation> findByTeacherId(Long id) {
        logger.debug("Find vacation width id: {}", id);
        return vacationRepository.findByTeacher_Id(id);
    }
    public List<Vacation> findAll() {
        logger.debug("Find all vacations");
        return vacationRepository.findAll();
    }

    public boolean isSingle(Vacation vacation) {
        logger.debug("Check vacation is single");
        return vacationRepository.existsByStartJobAndEndJobAndTeacher_Id(vacation.getStartJob(),vacation.getEndJob(),vacation.getTeacher().getId());
    }

    private boolean dataVacationCorrect(Vacation vacation) {
        return vacation.getStartJob().isBefore(vacation.getEndJob());
    }

    private boolean vacationMinAndMaxDayCorrect(Vacation vacation) {
        long daysBetween = ChronoUnit.DAYS.between(vacation.getStartJob(), vacation.getEndJob());
        return Math.abs(daysBetween) <= maxVacationDay && Math.abs(daysBetween) >= minVacationDay;
    }
}
