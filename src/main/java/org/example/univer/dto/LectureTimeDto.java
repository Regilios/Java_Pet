package org.example.univer.dto;

import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
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
    @NotNull(message = "{time.startLecture.notnull}")
    @FutureOrPresent(message = "{time.startLecture.future}")
    private LocalDateTime startLecture;
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    @NotNull(message = "{time.endLecture.notnull}")
    @FutureOrPresent(message = "{time.endLecture.future}")
    private LocalDateTime endLecture;
}
