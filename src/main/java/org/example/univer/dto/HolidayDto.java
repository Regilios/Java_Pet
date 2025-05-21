package org.example.univer.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Getter
@Setter
public class HolidayDto {
    private static final String DATE_PATTERN_HOLIDAY = "yyyy-MM-dd";
    private Long id;
    private String description;
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    private LocalDate startHoliday;
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    private LocalDate endHoliday;
}
