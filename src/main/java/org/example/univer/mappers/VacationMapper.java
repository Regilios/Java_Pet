package org.example.univer.mappers;

import org.example.univer.dto.VacationDto;
import org.example.univer.models.Vacation;
import org.example.univer.services.TeacherService;
import org.springframework.stereotype.Component;

@Component
public class VacationMapper {
    private final TeacherService teacherService;
    private final TeacherMapper teacherMapper;

    public VacationMapper(TeacherService teacherService, TeacherMapper teacherMapper) {
        this.teacherService = teacherService;
        this.teacherMapper = teacherMapper;
    }

    public Vacation toEntity(VacationDto dto) {
        Vacation vacation = new Vacation();
        vacation.setId(dto.getId());
        vacation.setStartJob(dto.getStartJob());
        vacation.setEndJob(dto.getEndJob());
        teacherService.findById(dto.getTeacher().getId()).ifPresent(vacation::setTeacher);

        return vacation;
    }

    public VacationDto toDto(Vacation vacation) {
        VacationDto dto = new VacationDto();
        dto.setId(vacation.getId());
        dto.setStartJob(vacation.getStartJob());
        dto.setEndJob(vacation.getEndJob());
        dto.setTeacher(teacherMapper.toDto(vacation.getTeacher()));

        return dto;
    }
}
