package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.exeption.HolidaysExeption;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.models.Holiday;
import org.example.univer.repositories.HolidayRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class HolidayService {
    private final HolidayRepository holidayRepository;
    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);
    private final AppSettings appSettings;
    private Integer maxDayHoliday;
    private String startDayHoliday;

    @PostConstruct
    public void init() {
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
                    throw new InvalidParameterException(String.format("Невозможно создать каникулы! Каникулы с названием: %s уже существуют!", holiday.getDescription()));
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

    public Holiday create(Holiday holiday) {
        logger.debug("Creating holiday: {}", holiday);
        validate(holiday, ValidationContext.METHOD_CREATE);
        return holidayRepository.save(holiday);
    }

    public Holiday update(Holiday holiday) {
        logger.debug("Updating holiday: {}", holiday);
        validate(holiday, ValidationContext.METHOD_UPDATE);
        return holidayRepository.save(holiday);
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

    public boolean existsById(Long id) {
        logger.debug("Check holiday is single");
        return holidayRepository.existsById(id);
    }
}
