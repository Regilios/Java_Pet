package org.example.univer.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class GroupDto {
    private Long id;
    @NotEmpty(message = "{group.name.notempty}")
    private String name;
    @NotNull(message = "{group.cathedra.notnull}")
    private CathedraDto cathedra;
}
