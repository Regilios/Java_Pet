
package org.example.univer.mappers;

import org.example.univer.dto.GroupDto;
import org.example.univer.dto.LectureDto;
import org.example.univer.models.Group;
import org.example.univer.models.Lecture;
import org.example.univer.services.*;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class LectureMapper {
    private final TeacherService teacherService;
    private final GroupService groupService;
    private final AudienceService audienceService;
    private final SubjectService subjectService;
    private final CathedraService cathedraService;
    private final LectureTimeService lectureTimeService;
    private final AudienceMapper audienceMapper;
    private final CathedraMapper cathedraMapper;
    private final SubjectMapper subjectMapper;
    private final LectureTimeMapper lectureTimeMapper;
    private final TeacherMapper teacherMapper;
    private final GroupMapper groupMapper;

    public LectureMapper(TeacherMapper teacherMapper,
                         LectureTimeMapper lectureTimeMapper,
                         SubjectMapper subjectMapper,
                         CathedraMapper cathedraMapper,
                         AudienceMapper audienceMapper,
                         TeacherService teacherService,
                         GroupService groupService,
                         AudienceService audienceService,
                         SubjectService subjectService,
                         CathedraService cathedraService,
                         LectureTimeService lectureTimeService,
                         GroupMapper groupMapper) {
        this.audienceMapper = audienceMapper;
        this.teacherService = teacherService;
        this.groupService = groupService;
        this.audienceService = audienceService;
        this.subjectService = subjectService;
        this.cathedraService = cathedraService;
        this.lectureTimeService = lectureTimeService;
        this.cathedraMapper = cathedraMapper;
        this.subjectMapper = subjectMapper;
        this.lectureTimeMapper = lectureTimeMapper;
        this.teacherMapper = teacherMapper;
        this.groupMapper = groupMapper;
    }

    public Lecture toEntity(LectureDto dto) {
        Lecture lecture = new Lecture();
        lecture.setId(dto.getId());

        teacherService.findById(dto.getTeacher().getId()).ifPresent(lecture::setTeacher);
        audienceService.findById(dto.getAudience().getId()).ifPresent(lecture::setAudience);
        subjectService.findById(dto.getSubject().getId()).ifPresent(lecture::setSubject);
        cathedraService.findById(dto.getCathedra().getId()).ifPresent(lecture::setCathedra);
        lectureTimeService.findById(dto.getTime().getId()).ifPresent(lecture::setTime);

        List<Group> groups = dto.getGroupIds().stream()
                .map(id -> groupService.findById(id).orElse(null))
                .collect(Collectors.toList());

        lecture.setGroups(groups);
        return lecture;
    }

    public LectureDto toDto(Lecture lecture) {
        LectureDto dto = new LectureDto();
        dto.setId(lecture.getId());
        dto.setTeacher(teacherMapper.toDto(lecture.getTeacher()));
        dto.setAudience(audienceMapper.toDto(lecture.getAudience()));
        dto.setSubject(subjectMapper.toDto(lecture.getSubject()));
        dto.setCathedra(cathedraMapper.toDto(lecture.getCathedra()));
        dto.setTime(lectureTimeMapper.toDto(lecture.getTime()));

        List<Long> groupIds = lecture.getGroups().stream()
                .map(Group::getId)
                .collect(Collectors.toList());
        dto.setGroupIds(groupIds);

        List<GroupDto> groups = lecture.getGroups().stream()
                .map(groupMapper::toDto)
                .collect(Collectors.toList());
        dto.setGroups(groups);

        return dto;
    }
}
