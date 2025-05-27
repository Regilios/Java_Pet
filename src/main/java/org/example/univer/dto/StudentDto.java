package org.example.univer.dto;

import lombok.Getter;
import lombok.Setter;
import org.example.univer.models.Gender;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class StudentDto {
    private Long id;
    private GroupDto group;
    protected String firstName;
    protected String lastName;
    private Gender gender;
    private String address;
    private String email;
    private String phone;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate birthday;
}
