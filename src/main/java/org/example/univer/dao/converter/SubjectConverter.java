package org.example.univer.dao.converter;

import org.example.univer.models.Subject;
import org.example.univer.services.SubjectService;
import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;

@Component
public class SubjectConverter implements Converter<String, Subject> {
    private SubjectService subjectService;
    public SubjectConverter(SubjectService subjectService) {
        this.subjectService = subjectService;
    }

    @Override
    public Subject convert(String source) {
        return subjectService.findById(Long.valueOf(source)).orElse(null);
    }
}