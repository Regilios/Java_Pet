package org.example.univer.mappers;

import org.example.univer.dto.AudienceDto;
import org.example.univer.models.Audience;
import org.springframework.stereotype.Component;

@Component
public class AudienceMapper {
    public Audience toEntity(AudienceDto dto) {
        Audience audience = new Audience();
        audience.setId(dto.getId());
        audience.setRoomNumber(dto.getRoomNumber());
        audience.setCapacity(dto.getCapacity());

        return audience;
    }

    public AudienceDto toDto(Audience audience) {
        AudienceDto dto = new AudienceDto();
        dto.setId(audience.getId());
        dto.setRoomNumber(audience.getRoomNumber());
        dto.setCapacity(audience.getCapacity());

        return dto;
    }
}
