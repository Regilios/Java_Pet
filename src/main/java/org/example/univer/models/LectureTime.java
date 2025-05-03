package org.example.univer.models;

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
                        query = "SELECT COUNT(*) FROM LectureTime WHERE startLection=:start_lection AND endLection=:end_lection "
                )
        })
@Entity
@Table(name = "lectiontime")
public class LectureTime implements Serializable {
    private static final long serialVersionUID = -4670760351992342275L;
    private static final String DATE_PATTERN_LECTURE_TIME = "yyyy-MM-dd HH:mm:ss";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_lection", nullable = false)
    @DateTimeFormat(pattern = DATE_PATTERN_LECTURE_TIME)
    private LocalDateTime startLection;
    @Column(name = "end_lection", nullable = false)
    @DateTimeFormat(pattern = DATE_PATTERN_LECTURE_TIME)
    private LocalDateTime endLection;

    public LectureTime(Long id, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.startLection = start;
        this.endLection = end;
    }

    public LectureTime() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartLection() {
        return Objects.nonNull(startLection) ? startLection.format(DateTimeFormatter.ofPattern(DATE_PATTERN_LECTURE_TIME)) : null;
    }
    public LocalDateTime getStartLocal() {
        return startLection;
    }

    public void setStartLection(LocalDateTime startLection) {
        this.startLection = startLection;
    }

    public String getEndLection() {
        return Objects.nonNull(endLection) ? endLection.format(DateTimeFormatter.ofPattern(DATE_PATTERN_LECTURE_TIME)) : null;
    }

    public LocalDateTime getEndLocal() {
        return endLection;
    }

    public void setEndLection(LocalDateTime endLection) {
        this.endLection = endLection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureTime that = (LectureTime) o;
        return Objects.equals(id, that.id) && Objects.equals(startLection, that.startLection) && Objects.equals(endLection, that.endLection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startLection, endLection);
    }
}
