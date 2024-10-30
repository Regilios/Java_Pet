package org.example.univer.dao.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Vacation implements Serializable {
    private static final long serialVersionUID = 2595365395005712101L;
    private Long id;
    private LocalDate startJob;
    private LocalDate endJob;
    private Teacher teacher;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM, yyyy");

    public Vacation(Long id, LocalDate startJob, LocalDate endJob, Teacher teacher) {
        this.id = id;
        this.startJob = startJob;
        this.endJob = endJob;
        this.teacher = teacher;
    }
    public Vacation() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getStartJob() {
        return startJob.format(formatter);
    }

    public void setStartJob(LocalDate startJob) {
        this.startJob = startJob;
    }

    public String getEndJob() {
        return endJob.format(formatter);
    }

    public void setEndJob(LocalDate endJob) {
        this.endJob = endJob;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }
    public String getTeacherFirstName() {
        return teacher.getFirstName();
    }
    public String getTeacherLastName() {
        return teacher.getLastName();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Vacation vacation = (Vacation) o;
        return Objects.equals(id, vacation.id) && Objects.equals(startJob, vacation.startJob) && Objects.equals(endJob, vacation.endJob);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, startJob, endJob);
    }
}
