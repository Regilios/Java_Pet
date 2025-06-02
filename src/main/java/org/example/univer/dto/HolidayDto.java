package org.example.univer.dto;

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
    private LocalDate startHoliday;
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    @NotNull(message = "{holiday.endHoliday.notnull}")
    @FutureOrPresent(message = "{holiday.endHoliday.future}")
    private LocalDate endHoliday;
}
