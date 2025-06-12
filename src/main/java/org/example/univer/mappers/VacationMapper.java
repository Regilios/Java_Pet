package org.example.univer.mappers;

import org.example.univer.dto.VacationDto;
import org.example.univer.models.Vacation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {TeacherMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface VacationMapper {
    Vacation toEntity(VacationDto dto);
    VacationDto toDto(Vacation vacation);
}