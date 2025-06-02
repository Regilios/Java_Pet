package org.example.univer.dto;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AudienceDto {
    private Long id;
    @NotNull(message = "{audience.roomNumber.notnull}")
    @Positive
    private Integer roomNumber;
    @NotNull(message = "{audience.capacity.notnull}")
    @Positive
    private Integer capacity;
}
