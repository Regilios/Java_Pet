package org.example.univer.mappers;

import org.example.univer.dto.VacationDto;
import org.example.univer.models.Vacation;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(
        componentModel = "spring",
        uses = {TeacherMapper.class},
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface VacationMapper {
    @Mapping(target = "teacher", ignore = true)
    Vacation toEntity(VacationDto dto);
    @Mapping(source = "teacher.id", target = "teacherId")
    @Mapping(source = "teacher.firstName", target = "teacherFirstName")
    @Mapping(source = "teacher.lastName", target = "teacherLastName")
    VacationDto toDto(Vacation vacation);
}