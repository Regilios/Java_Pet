package org.example.univer.mappers;

import org.example.univer.dto.GroupDto;
import org.example.univer.models.Group;
import org.example.univer.services.CathedraService;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@Transactional(readOnly = true)
public class GroupMapper {
    private final CathedraService cathedraService;
    private final CathedraMapper cathedraMapper;

    public GroupMapper(CathedraService cathedraService, CathedraMapper cathedraMapper) {
        this.cathedraService = cathedraService;
        this.cathedraMapper = cathedraMapper;
    }

    public Group toEntity(GroupDto dto) {
        Group group = new Group();
        group.setId(dto.getId());
        group.setName(dto.getName());
        cathedraService.findById(dto.getCathedra().getId()).ifPresent(group::setCathedra);

        return group;
    }

    public GroupDto toDto(Group group) {
        GroupDto dto = new GroupDto();
        dto.setId(group.getId());
        dto.setName(group.getName());
        dto.setCathedra(cathedraMapper.toDto(group.getCathedra()));

        return dto;
    }
}
