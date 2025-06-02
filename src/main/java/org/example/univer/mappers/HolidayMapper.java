package org.example.univer.mappers;

import org.example.univer.dto.HolidayDto;
import org.example.univer.models.Holiday;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class HolidayMapper {
    public Holiday toEntity(HolidayDto dto) {
        Holiday holiday = new Holiday();
        holiday.setId(dto.getId());
        holiday.setDescription(dto.getDescription());
        holiday.setStartHoliday(dto.getStartHoliday());
        holiday.setEndHoliday(dto.getEndHoliday());

        return holiday;
    }

    public HolidayDto toDto(Holiday holiday) {
        HolidayDto dto = new HolidayDto();
        dto.setId(holiday.getId());
        dto.setDescription(holiday.getDescription());
        dto.setStartHoliday(holiday.getStartHoliday());
        dto.setEndHoliday(holiday.getEndHoliday());

        return dto;
    }
}