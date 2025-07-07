package org.example.univer.mappers;

import org.example.univer.dto.LectureTimeDto;
import org.example.univer.models.LectureTime;
import org.mapstruct.InjectionStrategy;
import org.mapstruct.Mapper;

@Mapper(
        componentModel = "spring",
        injectionStrategy = InjectionStrategy.CONSTRUCTOR
)
public interface LectureTimeMapper {
     LectureTime toEntity(LectureTimeDto dto);
     LectureTimeDto toDto(LectureTime lectureTime);
}