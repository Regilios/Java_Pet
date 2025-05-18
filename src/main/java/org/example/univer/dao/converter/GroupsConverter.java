package org.example.univer.dao.converter;

import org.example.univer.models.Group;
import org.example.univer.services.GroupService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class GroupsConverter implements Converter<String, Group> {
    private GroupService groupService;
    public GroupsConverter(GroupService groupService) {
        this.groupService = groupService;
    }

    @Override
    public Group convert(String source) {
        Long id = Long.parseLong(source);
        return groupService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid group ID: " + id));
    }
}