package org.example.univer.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LectureTimeFormDto {
    @NotBlank(message = "{time.startLecture.notnull}")
    private String startDate;
    @NotBlank(message = "{time.startLecture.notnull}")
    private String startTime;
    @NotBlank(message = "{time.endLecture.notnull}")
    private String endDate;
    @NotBlank(message = "{time.endLecture.notnull}")
    private String endTime;
}