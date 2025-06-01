package org.example.univer.validators;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.example.univer.interfeses.CheckBirthday;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.Period;

@Component
public class CheckBirthdayValidator implements ConstraintValidator<CheckBirthday, LocalDate> {

    @Override
    public boolean isValid(LocalDate localDate, ConstraintValidatorContext context) {
        if (localDate == null) {
            return true;
        }

        LocalDate currentDate = LocalDate.now();
        if (localDate.isAfter(currentDate)) {
            return false;
        }

        boolean isValid = Period.between(localDate, currentDate).getYears() >= 18;

        if (!isValid) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Возраст должен быть ≥18 лет")
                    .addConstraintViolation();
        }

        return isValid;
    }
}