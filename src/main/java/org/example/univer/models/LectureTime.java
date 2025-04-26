package org.example.univer.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class LectureTime implements Serializable {
    private static final long serialVersionUID = 4670760351992342275L;
    private Long id;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime start_lection;
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    private LocalDateTime end_lection;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

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
        return start_lection.format(formatter);
    }

    public LocalDateTime getStartLocal() {
        return start_lection;
    }

    public void setStart_lection(LocalDateTime start_lection) {
        this.start_lection = start_lection;
    }

    public String getEnd_lection() {
        return end_lection.format(formatter);
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
