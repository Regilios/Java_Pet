package org.example.univer.mappers;

import org.example.univer.dto.SubjectDto;
import org.example.univer.models.Subject;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface SubjectMapper {
    Subject toEntity(SubjectDto dto);
    SubjectDto toDto(Subject subject);
}