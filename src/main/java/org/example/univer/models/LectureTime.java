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
                        query = "SELECT COUNT(*) FROM LectureTime WHERE start_lection=:start_lection AND end_lection=:end_lection "
                )
        })
@Entity
@Table(name = "lectiontime")
public class LectureTime implements Serializable {
    private static final long serialVersionUID = -4670760351992342275L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "start_lection", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start_lection;
    @Column(name = "end_lection", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end_lection;

    public LectureTime(Long id, LocalDateTime start, LocalDateTime end) {
        this.id = id;
        this.start_lection = start;
        this.end_lection = end;
    }

    public LectureTime() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStart_lection() {
        return start_lection != null ? start_lection.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }
    public LocalDateTime getStartLocal() {
        return start_lection;
    }

    public void setStart_lection(LocalDateTime start_lection) {
        this.start_lection = start_lection;
    }

    public String getEnd_lection() {
        return end_lection != null ? end_lection.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    public LocalDateTime getEndLocal() {
        return end_lection;
    }

    public void setEnd_lection(LocalDateTime end_lection) {
        this.end_lection = end_lection;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LectureTime that = (LectureTime) o;
        return Objects.equals(id, that.id) && Objects.equals(start_lection, that.start_lection) && Objects.equals(end_lection, that.end_lection);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, start_lection, end_lection);
    }
}
