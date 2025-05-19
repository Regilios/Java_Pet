package org.example.univer.mappers;

import org.example.univer.dto.CathedraDto;
import org.example.univer.models.Cathedra;
import org.springframework.stereotype.Component;

@Component
public class CathedraMapper {
    public Cathedra toEntity(CathedraDto dto) {
        Cathedra cathedra = new Cathedra();
        cathedra.setId(dto.getId());
        cathedra.setName(dto.getName());

        return cathedra;
    }

    public CathedraDto toDto(Cathedra cathedra) {
        CathedraDto dto = new CathedraDto();
        dto.setId(cathedra.getId());
        dto.setName(cathedra.getName());

        return dto;
    }
}
