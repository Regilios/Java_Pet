package org.example.univer.dto;

import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;

@Getter
@Setter
public class LectureTimeDto {
    private static final String DATE_PATTERN_HOLIDAY = "yyyy-MM-dd HH:mm:ss";
    private Long id;
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    private LocalDateTime startLecture;
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    private LocalDateTime endLecture;
}
