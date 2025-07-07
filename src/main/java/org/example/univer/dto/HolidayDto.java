package org.example.univer.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class HolidayDto {
    private static final String DATE_PATTERN_HOLIDAY = "yyyy-MM-dd";
    private Long id;
    @NotEmpty(message = "{holiday.description.notnull}")
    private String description;
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    @NotNull(message = "{holiday.endHoliday.notnull}")
    @FutureOrPresent(message = "{holiday.startHoliday.future}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startHoliday;
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    @NotNull(message = "{holiday.endHoliday.notnull}")
    @FutureOrPresent(message = "{holiday.endHoliday.future}")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endHoliday;
}
