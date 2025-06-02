package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "lectiontime")
public class LectureTime implements Serializable {
    private static final long serialVersionUID = -4670760351992342275L;
    private static final String DATE_PATTERN_LECTURE_TIME = "yyyy-MM-dd HH:mm:ss";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "start_lection", nullable = false)
    @DateTimeFormat(pattern = DATE_PATTERN_LECTURE_TIME)
    private LocalDateTime startLecture;

    @Column(name = "end_lection", nullable = false)
    @DateTimeFormat(pattern = DATE_PATTERN_LECTURE_TIME)
    private LocalDateTime endLecture;
}
