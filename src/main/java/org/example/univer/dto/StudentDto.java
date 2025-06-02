package org.example.univer.dto;

import jakarta.validation.constraints.*;
import lombok.Getter;
import lombok.Setter;
import org.example.univer.interfeses.CheckBirthday;
import org.example.univer.models.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class StudentDto {
    private Long id;
    @NotNull(message = "{student.group.notnull}")
    private GroupDto group;
    @NotBlank(message = "{student.firstName.notblank}")
    protected String firstName;
    @NotBlank(message = "{student.lastName.notblank}")
    protected String lastName;
    private Gender gender;
    @NotBlank(message = "{student.address.notblank}")
    private String address;
    @NotBlank(message = "{student.email.notblank}")
    @Email
    private String email;
    @Pattern(regexp = "\\+7\\d{10}", message = "{student.phone.pattern}")
    private String phone;
    @NotNull(message = "{student.birthday.notnull}")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @Past(message = "{student.birthday.past}")
    @CheckBirthday
    private LocalDate birthday;
}
