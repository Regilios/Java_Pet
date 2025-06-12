package org.example.univer.mappers;

import org.example.univer.dto.GroupDto;
import org.example.univer.models.Group;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring", uses = {CathedraMapper.class}, injectionStrategy = InjectionStrategy.CONSTRUCTOR)
public interface GroupMapper {
    Group toEntity(GroupDto dto);
    GroupDto toDto(Group group);
}