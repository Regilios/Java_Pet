package org.example.univer.mappers;

import org.example.univer.dto.AudienceDto;
import org.example.univer.models.Audience;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface AudienceMapper {
    AudienceDto toDto(Audience audience);
    Audience toEntity(AudienceDto audienceDto);
}