package org.example.univer.dto;

import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Getter
@Setter
public class HolidayDto {
    private Long id;
    private String description;
    private LocalDate startHoliday;
    private LocalDate endHoliday;
}
