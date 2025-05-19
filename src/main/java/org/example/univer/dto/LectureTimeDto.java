package org.example.univer.dto;

import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@Getter
@Setter
public class LectureTimeDto {
    private Long id;
    private LocalDateTime startLecture;
    private LocalDateTime endLecture;
}
