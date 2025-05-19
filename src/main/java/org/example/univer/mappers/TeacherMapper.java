package org.example.univer.mappers;

import org.example.univer.dto.TeacherDto;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.example.univer.services.CathedraService;
import org.example.univer.services.SubjectService;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

@Component
public class TeacherMapper {
    private final SubjectService subjectService;
    private final SubjectMapper subjectMapper;
    private final CathedraService cathedraService;
    private final CathedraMapper cathedraMapper;

    public TeacherMapper(SubjectMapper subjectMapper,
                         SubjectService subjectService,
                         CathedraService cathedraService,
                         CathedraMapper cathedraMapper) {
        this.subjectService = subjectService;
        this.subjectMapper = subjectMapper;
        this.cathedraService = cathedraService;
        this.cathedraMapper = cathedraMapper;
    }

    public Teacher toEntity(TeacherDto dto) {
        Teacher teacher = new Teacher();
        teacher.setId(dto.getId());
        teacher.setFirstName(dto.getFirstName());
        teacher.setLastName(dto.getLastName());
        teacher.setGender(dto.getGender());
        teacher.setAddress(dto.getAddress());
        teacher.setEmail(dto.getEmail());
        teacher.setPhone(dto.getPhone());
        teacher.setBirthday(dto.getBirthday());
        cathedraService.findById(dto.getCathedra().getId()).ifPresent(teacher::setCathedra);
        List<Subject> subjects = dto.getSubjectIds().stream()
                .map(id -> subjectService.findById(id).orElse(null))
                .collect(Collectors.toList());

        teacher.setSubjects(subjects);
        return teacher;
    }

    public TeacherDto toDto(Teacher teacher) {
        TeacherDto dto = new TeacherDto();
        dto.setId(teacher.getId());
        dto.setFirstName(teacher.getFirstName());
        dto.setLastName(teacher.getLastName());
        dto.setGender(teacher.getGender());
        dto.setAddress(teacher.getAddress());
        dto.setEmail(teacher.getEmail());
        dto.setPhone(teacher.getPhone());
        dto.setBirthday(teacher.getBirthday());
        dto.setCathedra(cathedraMapper.toDto(teacher.getCathedra()));

        List<Long> subjectsIds = teacher.getSubjects().stream()
                .map(Subject::getId)
                .collect(Collectors.toList());
        dto.setSubjectIds(subjectsIds);

        return dto;
    }
}
