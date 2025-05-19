package org.example.univer.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LectureDto {
    private Long id;
    private TeacherDto teacher;
    private SubjectDto subject;
    private CathedraDto cathedra;
    private AudienceDto audience;
    private LectureTimeDto time;
    private List<GroupDto> groups;
    private List<Long> groupIds;
}
