package org.example.univer.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class VacationDto {
    private static final String DATE_PATTERN_VACATION = "yyyy-MM-dd";
    private Long id;
    @DateTimeFormat(pattern = DATE_PATTERN_VACATION)
    @NotNull(message = "{vacation.startJob.notnull}")
    private LocalDate startJob;
    @DateTimeFormat(pattern = DATE_PATTERN_VACATION)
    @NotNull(message = "{vacation.endJob.notnull}")
    private LocalDate endJob;
    private TeacherDto teacher;
}
