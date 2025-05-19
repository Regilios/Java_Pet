package org.example.univer.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
@NamedQueries(
        {
                @NamedQuery(
                        name = "findAllLectureTime",
                        query = "FROM LectureTime"
                ),
                @NamedQuery(
                        name = "findLectureTime",
                        query = "SELECT COUNT(*) FROM LectureTime WHERE startLecture=:startLecture AND endLecture=:endLecture "
                )
        })
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
