package org.example.univer.dao.converter;

import org.example.univer.models.Subject;
import org.example.univer.services.SubjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubjectConverter implements Converter<String, Subject> {

    @Autowired
    private SubjectService subjectService;

    @Override
    public Subject convert(String source) {
        Long id = Long.parseLong(source);
        return subjectService.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Invalid subject ID: " + id));
    }
}