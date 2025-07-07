package org.example.univer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
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
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startJob;
    @DateTimeFormat(pattern = DATE_PATTERN_VACATION)
    @NotNull(message = "{vacation.endJob.notnull}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endJob;
    private Long teacherId;
    private String teacherFirstName;
    private String teacherLastName;
}
