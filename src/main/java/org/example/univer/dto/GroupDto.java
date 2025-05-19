package org.example.univer.dto;

import lombok.Getter;
import lombok.Setter;
@Getter
@Setter
public class GroupDto {
    private Long id;
    private String name;
    private CathedraDto cathedra;
}
