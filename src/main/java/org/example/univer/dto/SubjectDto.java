package org.example.univer.dto;

import jakarta.validation.constraints.NotEmpty;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class SubjectDto {
    private Long id;
    @NotEmpty(message = "{subject.name.notempty}")
    private String name;
    @NotEmpty(message = "{subject.description.notempty}")
    private String description;
}
