package org.example.univer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
public class VacationDto {
    private Long id;
    private LocalDate startJob;
    private LocalDate endJob;
    private TeacherDto teacher;
}
