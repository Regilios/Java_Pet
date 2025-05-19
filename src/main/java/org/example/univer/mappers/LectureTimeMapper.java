package org.example.univer.mappers;

import org.example.univer.dto.LectureTimeDto;
import org.example.univer.models.LectureTime;
import org.springframework.stereotype.Component;

@Component
public class LectureTimeMapper {
    public LectureTime toEntity(LectureTimeDto dto) {
        LectureTime lectureTime = new LectureTime();
        lectureTime.setId(dto.getId());
        lectureTime.setStartLecture(dto.getStartLecture());
        lectureTime.setEndLecture(dto.getEndLecture());

        return lectureTime;
    }

    public LectureTimeDto toDto(LectureTime lectureTime) {
        LectureTimeDto dto = new LectureTimeDto();
        dto.setId(lectureTime.getId());
        dto.setStartLecture(lectureTime.getStartLecture());
        dto.setEndLecture(lectureTime.getEndLecture());

        return dto;
    }
}