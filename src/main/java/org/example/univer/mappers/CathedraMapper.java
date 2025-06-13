package org.example.univer.mappers;

import org.example.univer.dto.CathedraDto;
import org.example.univer.models.Cathedra;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface CathedraMapper {
     Cathedra toEntity(CathedraDto dto);
     CathedraDto toDto(Cathedra cathedra);
}