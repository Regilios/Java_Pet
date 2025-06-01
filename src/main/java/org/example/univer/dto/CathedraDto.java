package org.example.univer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CathedraDto {
    private Long id;
    @NotBlank(message = "{cathedra.name.notblank}")
    private String name;
}
