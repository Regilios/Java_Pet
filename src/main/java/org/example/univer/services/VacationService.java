package org.example.univer.services;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.example.univer.config.AppSettings;
import org.example.univer.exeption.InvalidParameterException;
import org.example.univer.exeption.ResourceNotFoundException;
import org.example.univer.exeption.VacationExeption;
import org.example.univer.models.Teacher;
import org.example.univer.models.Vacation;
import org.example.univer.repositories.VacationRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class VacationService {
    private final VacationRepository vacationRepository;
    private final TeacherService teacherService;
    private static final Logger logger = LoggerFactory.getLogger(VacationService.class);
    private final AppSettings appSettings;
    private Integer minVacationDay;
    private Integer maxVacationDay;

    @PostConstruct
    public void init() {
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

    public Vacation create(Vacation vacation) {
        logger.debug("Creating vacation: {}", vacation);
        validate(vacation, VacationService.ValidationContext.METHOD_CREATE);
        return vacationRepository.save(vacation);
    }

    public Vacation update(Vacation vacation) {
        logger.debug("Updating vacation: {}", vacation);
        validate(vacation, VacationService.ValidationContext.METHOD_UPDATE);
        return vacationRepository.save(vacation);
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

    public Teacher findTeacherById(Long id) {
        return teacherService.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Преподаватель с id " + id + " не найден"));
    }

    public List<Vacation> findAll() {
        logger.debug("Find all vacations");
        return vacationRepository.findAll();
    }

    public boolean isSingle(Vacation vacation) {
        logger.debug("Check vacation is single");
        return vacationRepository.existsByStartJobAndEndJobAndTeacher_Id(vacation.getStartJob(),vacation.getEndJob(),vacation.getTeacher().getId());
    }

    public boolean existsById(Long id) {
        logger.debug("Check vacation is single");
        return vacationRepository.existsById(id);
    }

    private boolean dataVacationCorrect(Vacation vacation) {
        return vacation.getStartJob().isBefore(vacation.getEndJob());
    }

    private boolean vacationMinAndMaxDayCorrect(Vacation vacation) {
        long daysBetween = ChronoUnit.DAYS.between(vacation.getStartJob(), vacation.getEndJob());
        return Math.abs(daysBetween) <= maxVacationDay && Math.abs(daysBetween) >= minVacationDay;
    }
}
