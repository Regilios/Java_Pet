package org.example.univer.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.Setter;
import org.example.univer.models.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Getter
@Setter
public class TeacherDto {
    private Long id;
    private List<SubjectDto> subjects = new ArrayList<>();
    @NotNull(message = "{teacher.subjects.notnull}")
    private List<Long> subjectIds = new ArrayList<>();
    private List<VacationDto> vacation;
    @NotNull(message = "{teacher.cathedra.notnull}")
    private CathedraDto cathedra;
    @NotBlank(message = "{teacher.firstName.notblank}")
    protected String firstName;
    @NotBlank(message = "{teacher.lastName.notblank}")
    protected String lastName;
    private Gender gender;
    @NotBlank(message = "{teacher.address.notblank}")
    private String address;
    @NotBlank(message = "{teacher.email.notblank}")
    @Email
    private String email;
    @Pattern(regexp = "\\+7\\d{10}", message = "{teacher.phone.pattern}")
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "{teacher.birthday.notnull}")
    private LocalDate birthday;
}
