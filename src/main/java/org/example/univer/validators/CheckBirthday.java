package org.example.univer.validators;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = CheckBirthdayValidator.class)
@Target({ ElementType.FIELD })
@Retention(RetentionPolicy.RUNTIME)
public @interface CheckBirthday {
    String message() default "{student.birthday.adult}";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
