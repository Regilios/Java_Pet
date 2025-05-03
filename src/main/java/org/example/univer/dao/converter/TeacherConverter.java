package org.example.univer.dao.converter;

import org.example.univer.models.Teacher;
import org.example.univer.services.TeacherService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;


@Component
public class TeacherConverter implements Converter<String, Teacher> {

    @Autowired
    private TeacherService teacherService;

    @Override
    public Teacher convert(String source) {
        Long id = Long.parseLong(source);
        return teacherService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid teacher ID: " + id));
    }
}