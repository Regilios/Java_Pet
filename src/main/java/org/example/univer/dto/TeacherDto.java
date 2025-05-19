package org.example.univer.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.univer.models.Gender;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
public class TeacherDto {
    private Long id;
    private List<StudentDto> subjects;
    private List<Long> subjectIds;
    private List<VacationDto> vacation;
    private CathedraDto cathedra;
    protected String firstName;
    protected String lastName;
    private Gender gender;
    private String address;
    private String email;
    private String phone;
    private LocalDate birthday;
}
