package org.example.univer.services;

import org.example.univer.config.AppSettings;
import org.example.univer.repositories.HolidayRepository;
import org.example.univer.exeption.*;
import org.example.univer.models.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
public class HolidayService {
    HolidayRepository holidayRepository;
    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);
    private AppSettings appSettings;
    private Integer maxDayHoliday;
    private String startDayHoliday;

    @Autowired
    public HolidayService(HolidayRepository holidayRepository, AppSettings appSettings) {
        this.holidayRepository = holidayRepository;
        this.appSettings = appSettings;
        this.maxDayHoliday = appSettings.getMaxDayHoliday();
        this.startDayHoliday = appSettings.getStartDayHoliday();
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Holiday holiday, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(holiday)) {
                    throw new InvalidParameterException("Невозможно создать каникулы! Каникулы с названием: " + holiday.getDescription() + " уже существуют!");
                }
                validateCommon(holiday, "создать");
                break;

            case METHOD_UPDATE:
                validateCommon(holiday, "обновить");
                break;

            default:
                return "Контекст валидации отсутствует или неизвестен: " + context;
        }
        return null;
    }

    private void validateCommon(Holiday holiday, String action) {
        if (!holiday.getStartHoliday().getDayOfWeek().equals(DayOfWeek.valueOf(startDayHoliday))) {
            throw new HolidaysExeption("Невозможно " + action + " каникулы! Каникулы должны начинаться с " + startDayHoliday + "!");
        }
        if (ChronoUnit.DAYS.between(holiday.getStartHoliday(), holiday.getEndHoliday()) > maxDayHoliday) {
            throw new HolidaysExeption("Невозможно " + action + " каникулы! Каникулы не должны превышать заданное количество дней отдыха: " + maxDayHoliday + "!");
        }
    }

    public void create(Holiday holiday) {
        logger.debug("Start create holiday");
        try {
            validate(holiday, ValidationContext.METHOD_CREATE);
            holidayRepository.save(holiday);
            logger.debug("Holiday created");
        } catch (HolidaysExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта праздника: {}", e.getMessage(), e);
            throw new NullEntityException("Объект праздника не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта праздника: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта праздника", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект праздника не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта праздника", e);
        }
        logger.debug("Holiday created");
    }

    public void update(Holiday holiday) {
        logger.debug("Start update holiday");
        try {
            validate(holiday, ValidationContext.METHOD_UPDATE);
            holidayRepository.save(holiday);
            logger.debug("Holiday updated");
        } catch (HolidaysExeption e) {
            logger.error("Ошибка: {}", e.getMessage(), e);
            throw e;
        } catch (NullPointerException e) {
            logger.error("NullPointerException при создании объекта праздника: {}", e.getMessage(), e);
            throw new NullEntityException("Объект праздника не может быть null", e);
        } catch (IllegalArgumentException e) {
            logger.error("IllegalArgumentException при создании объекта праздника: {}", e.getMessage(), e);
            throw new InvalidParameterException("Неправильный аргумент для создания объекта праздника", e);
        } catch (EmptyResultDataAccessException e) {
            logger.error("EmptyResultDataAccessException при создании объекта: {}", e.getMessage(), e);
            throw new EntityNotFoundException("Объект праздника не найден", e);
        } catch (Exception e) {
            logger.error("Неизвестная ошибка при создании объекта: {}", e.getMessage(), e);
            throw new ServiceException("Неизвестная ошибка при создании объекта праздника", e);
        }
        logger.debug("Holiday updated");
    }

    public void deleteById(Long id) {
        logger.debug("Delete holiday width id: {}", id);
        holidayRepository.deleteById(id);
    }

    public Optional<Holiday> findById(Long id) {
        logger.debug("Find holiday width id: {}", id);
        return holidayRepository.findById(id);
    }

    public List<Holiday> findAll() {
        logger.debug("Find all holidays");
        return holidayRepository.findAll();
    }

    public boolean isSingle(Holiday holiday) {
        logger.debug("Check holiday is single");
        return holidayRepository.existsByDescription(holiday.getDescription());
    }
}
