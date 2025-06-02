package org.example.univer.dto;

import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class LectureDto {
    private Long id;
    @NotNull(message = "{lecture.teacher.notnull}")
    private TeacherDto teacher;
    @NotNull(message = "{lecture.subject.notnull}")
    private SubjectDto subject;
    @NotNull(message = "{lecture.cathedra.notnull}")
    private CathedraDto cathedra;
    @NotNull(message = "{lecture.audience.notnull}")
    private AudienceDto audience;
    @NotNull(message = "{lecture.time.notnull}")
    private LectureTimeDto time;
    private List<GroupDto> groups;
    @NotEmpty(message = "{lecture.groups.notnull}")
    private List<Long> groupIds;
}
