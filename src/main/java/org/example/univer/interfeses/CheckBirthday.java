package org.example.univer.interfeses;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import org.example.univer.validators.CheckBirthdayValidator;

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
