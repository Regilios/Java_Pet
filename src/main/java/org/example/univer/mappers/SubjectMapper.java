package org.example.univer.mappers;

import org.example.univer.dto.SubjectDto;
import org.example.univer.models.Subject;
import org.springframework.stereotype.Component;

@Component
public class SubjectMapper {
    public Subject toEntity(SubjectDto dto) {
        Subject subject = new Subject();
        subject.setId(dto.getId());
        subject.setName(dto.getName());
        subject.setDescription(dto.getDescription());

        return subject;
    }

    public SubjectDto toDto(Subject subject) {
        SubjectDto dto = new SubjectDto();
        dto.setId(subject.getId());
        dto.setName(subject.getName());
        dto.setDescription(subject.getDescription());

        return dto;
    }
}