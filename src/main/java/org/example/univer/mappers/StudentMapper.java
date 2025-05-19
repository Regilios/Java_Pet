package org.example.univer.mappers;

import org.example.univer.dto.StudentDto;
import org.example.univer.models.Student;
import org.example.univer.services.GroupService;
import org.springframework.stereotype.Component;

@Component
public class StudentMapper {
    private final GroupMapper groupMapper;
    private final GroupService groupService;

    public StudentMapper(GroupMapper groupMapper, GroupService groupService) {
        this.groupMapper = groupMapper;
        this.groupService = groupService;
    }

    public Student toEntity(StudentDto dto) {
        Student student = new Student();
        student.setId(dto.getId());
        student.setFirstName(dto.getFirstName());
        student.setLastName(dto.getLastName());
        student.setGender(dto.getGender());
        student.setAddress(dto.getAddress());
        student.setEmail(dto.getEmail());
        student.setPhone(dto.getPhone());
        student.setBirthday(dto.getBirthday());
        groupService.findById(dto.getGroup().getId()).ifPresent(student::setGroup);

        return student;
    }

    public StudentDto toDto(Student student) {
        StudentDto dto = new StudentDto();
        dto.setId(student.getId());
        dto.setFirstName(student.getFirstName());
        dto.setLastName(student.getLastName());
        dto.setGender(student.getGender());
        dto.setAddress(student.getAddress());
        dto.setEmail(student.getEmail());
        dto.setPhone(student.getPhone());
        dto.setBirthday(student.getBirthday());
        dto.setGroup(groupMapper.toDto(student.getGroup()));

        return dto;
    }
}