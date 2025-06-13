package org.example.univer.mappers;

import org.example.univer.dto.HolidayDto;
import org.example.univer.models.Holiday;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface HolidayMapper {
     Holiday toEntity(HolidayDto dto);
     HolidayDto toDto(Holiday holiday);
}