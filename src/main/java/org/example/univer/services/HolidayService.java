package org.example.univer.services;

import org.example.univer.dao.interfaces.DaoHolidayInterface;
import org.example.univer.models.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.stereotype.Service;

import java.time.DayOfWeek;
import java.time.temporal.ChronoUnit;
import java.util.List;

@Service
public class HolidayService {
    DaoHolidayInterface daoHolidayInterface;
    private static final Logger logger = LoggerFactory.getLogger(HolidayService.class);

    @Value("#{${maxDayHoliday}}")
    private Integer maxDayHoliday;

    @Value("#{${startDayHoliday}}")
    private String startDayHoliday;

    @Autowired
    public HolidayService(DaoHolidayInterface daoHolidayInterface) {
        this.daoHolidayInterface = daoHolidayInterface;
    }

    public enum ValidationContext {
        METHOD_CREATE,
        METHOD_UPDATE
    }

    public String validate(Holiday holiday, ValidationContext context) {
        switch (context) {
            case METHOD_CREATE:
                if (isSingle(holiday)) {
                    throw new IllegalArgumentException("Невозможно создать каникулы! Каникулы с названием: " + holiday.getDesc() + " уже существуют!");
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
        if (!holiday.getStartHolidayLocal().getDayOfWeek().equals(DayOfWeek.valueOf(startDayHoliday))) {
            throw new IllegalArgumentException("Невозможно " + action + " каникулы! Каникулы должны начинаться с " + startDayHoliday + "!");
        }
        if (ChronoUnit.DAYS.between(holiday.getStartHolidayLocal(), holiday.getEndHolidayLocal()) > maxDayHoliday) {
            throw new IllegalArgumentException("Невозможно " + action + " каникулы! Каникулы не должны превышать заданное количество дней отдыха: " + maxDayHoliday + "!");
        }
    }

    public void create(Holiday holiday) {
        logger.debug("Start create holiday");
        try {
            validate(holiday, ValidationContext.METHOD_CREATE);
            daoHolidayInterface.create(holiday);
            logger.debug("Holiday created");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void update(Holiday holiday) {
        logger.debug("Start update holiday");
        try {
            validate(holiday, ValidationContext.METHOD_UPDATE);
            daoHolidayInterface.update(holiday);
            logger.debug("Holiday updated");
        } catch (NullPointerException | IllegalArgumentException | EmptyResultDataAccessException e) {
            System.out.println(e.getMessage());
        } catch (Exception e) {
            System.out.println("Неизвестная ошибка: " + e.getMessage());
        }
    }

    public void deleteById(Long id) {
        logger.debug("Delete holiday width id: {}", id);
        daoHolidayInterface.deleteById(id);
    }

    public Holiday findById(Long id) {
        logger.debug("Find holiday width id: {}", id);
        return daoHolidayInterface.findById(id);
    }

    public List<Holiday> findAll() {
        logger.debug("Find all holidays");
        return daoHolidayInterface.findAll();
    }

    public boolean isSingle(Holiday holiday) {
        logger.debug("Check holiday is single");
        return daoHolidayInterface.isSingle(holiday);
    }
}
