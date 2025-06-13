
package org.example.univer.mappers;

import org.example.univer.dto.LectureDto;
import org.example.univer.exeption.EntityNotFoundException;
import org.example.univer.models.Group;
import org.example.univer.models.Lecture;
import org.example.univer.services.*;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                TeacherMapper.class,
                AudienceMapper.class,
                SubjectMapper.class,
                CathedraMapper.class,
                LectureTimeMapper.class,
                GroupMapper.class
        },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public abstract class LectureMapper {
    @Autowired protected TeacherService teacherService;
    @Autowired protected AudienceService audienceService;
    @Autowired protected SubjectService subjectService;
    @Autowired protected CathedraService cathedraService;
    @Autowired protected LectureTimeService lectureTimeService;
    @Autowired protected GroupService groupService;

    @Mapping(target = "teacher", ignore = true)
    @Mapping(target = "audience", ignore = true)
    @Mapping(target = "subject", ignore = true)
    @Mapping(target = "cathedra", ignore = true)
    @Mapping(target = "time", ignore = true)
    @Mapping(target = "groups", ignore = true)
    public abstract Lecture toEntity(LectureDto dto);

    @AfterMapping
    protected void afterDtoToEntity(LectureDto dto, @MappingTarget Lecture lect) {
        teacherService.findById(dto.getTeacher().getId())
                .ifPresentOrElse(lect::setTeacher, () -> { throw new EntityNotFoundException("Teacher not found"); });
        audienceService.findById(dto.getAudience().getId())
                .ifPresentOrElse(lect::setAudience, () -> { throw new EntityNotFoundException("Audience not found"); });
        subjectService.findById(dto.getSubject().getId())
                .ifPresentOrElse(lect::setSubject, () -> { throw new EntityNotFoundException("Subject not found"); });
        cathedraService.findById(dto.getCathedra().getId())
                .ifPresentOrElse(lect::setCathedra, () -> { throw new EntityNotFoundException("Cathedra not found"); });
        lectureTimeService.findById(dto.getTime().getId())
                .ifPresentOrElse(lect::setTime, () -> { throw new EntityNotFoundException("Time slot not found"); });

        List<Group> groups = dto.getGroupIds().stream()
                .map(id -> groupService.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Group not found: " + id)))
                .toList();
        lect.setGroups(groups);
    }

    @Mapping(target = "groupIds", expression = "java(lecture.getGroups().stream().map(Group::getId).toList())")
    public abstract LectureDto toDto(Lecture lecture);
}