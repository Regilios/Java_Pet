package org.example.univer.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;
import java.util.Objects;

@Component
public class CheckBirthdayValidator implements ConstraintValidator<CheckBirthday, LocalDate> {

    @Override
    public boolean isValid(LocalDate birthday, ConstraintValidatorContext context) {
        if (Objects.isNull(birthday)) {
            return true;
        }

        LocalDate currentDate = LocalDate.now();
        if (birthday.isAfter(currentDate)) {
            return false;
        }

        boolean isValid = Period.between(birthday, currentDate).getYears() >= 18;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Возраст должен быть ≥18 лет");
        }

        return isValid;
    }
}