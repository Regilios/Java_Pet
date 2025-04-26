package org.example.univer.models;

import org.springframework.format.annotation.DateTimeFormat;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
@NamedQueries({
        @NamedQuery(
                name = "findAllVacation",
                query = "FROM Vacation"
        ),
        @NamedQuery(
                name = "countVacantion",
                query = "SELECT COUNT(v) FROM Vacation v WHERE v.startJob =:startJob AND v.endJob =:endJob AND v.teacher.id =:teacher_id"
        ),
        @NamedQuery(
                name =  "findListVacantion",
                query = "FROM Vacation v WHERE v.teacher.id = :teacher_id"
        )
})
@Entity
@Table(name = "vacation")
public class Vacation implements Serializable {
    private static final long serialVersionUID = -2595365395005712101L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "startJob", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate startJob;

    @Column(name = "endJob", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate endJob;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

    public Vacation(Long id, LocalDate startJob, LocalDate endJob, Teacher teacher) {
        this.id = id;
        this.startJob = startJob;
        this.endJob = endJob;
        this.teacher = teacher;
    }

    public Vacation() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setStartJob(LocalDate startJob) {
        this.startJob = startJob;
    }

    public String getStartJobString() {
        return startJob != null ? startJob.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }

    public LocalDate getStartJob() {
        return startJob;
    }

    public void setEndJob(LocalDate endJob) {
        this.endJob = endJob;
    }

    public String getEndJobString() {
        return endJob != null ? endJob.format(DateTimeFormatter.ofPattern("yyyy-MM-dd")) : null;
    }

    public LocalDate getEndJob() {
        return endJob;
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
