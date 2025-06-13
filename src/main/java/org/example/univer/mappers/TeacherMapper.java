package org.example.univer.mappers;

import org.example.univer.dto.TeacherDto;
import org.example.univer.exeption.EntityNotFoundException;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.example.univer.services.CathedraService;
import org.example.univer.services.SubjectService;
import org.mapstruct.*;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

@Mapper(
        componentModel = "spring",
        uses = {
                SubjectMapper.class,
                CathedraMapper.class,
                VacationMapper.class
        },
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public abstract class TeacherMapper {
    @Autowired
    protected SubjectService subjectService;
    @Autowired
    protected CathedraService cathedraService;

    @Mapping(target = "subjects", ignore = true)
    @Mapping(target = "cathedra", ignore = true)
    public abstract Teacher toEntity(TeacherDto dto);

    @AfterMapping
    protected void afterDtoToEntity(TeacherDto dto, @MappingTarget Teacher teacher) {
        cathedraService.findById(dto.getCathedra().getId())
                .ifPresentOrElse(teacher::setCathedra, () -> { throw new EntityNotFoundException("Cathedra not found");
        });
        List<Subject> subjects = dto.getSubjectIds().stream()
                .map(id -> subjectService.findById(id)
                        .orElseThrow(() -> new EntityNotFoundException("Subject not found: " + id)))
                .toList();

        teacher.setSubjects(subjects);
    }

    @Mapping(target = "subjectIds", expression = "java(mapSubjectIds(teacher))")
    public abstract TeacherDto toDto(Teacher teacher);

    protected List<Long> mapSubjectIds(Teacher teacher) {
        return teacher.getSubjects().stream().map(Subject::getId).toList();
    }
}