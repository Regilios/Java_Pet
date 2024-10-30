package org.example.univer.dao.models;

import javax.security.auth.Subject;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Teacher extends Person implements Serializable {
    private static final long serialVersionUID = -2167704875913420388L;
    private Long id;
    private List<Subject> subject = new ArrayList<>();
    private Cathedra cathedra;
    private List<Vacation> vacation = new ArrayList<>();

    public Teacher(String firstName, String lastName, Gender gender, String addres, String email, String phone, LocalDate birthday, Long id, Cathedra cathedra, List<Subject> subject, List<Vacation> vacation ) {
        super(firstName, lastName, gender, addres, email, phone, birthday);
        this.id = id;
        this.cathedra = cathedra;
        this.subject = subject;
        this.vacation = vacation;
    }
    public Teacher() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Subject> getSubject() {
        return subject;
    }

    public void setSubject(List<Subject> subject) {
        this.subject = subject;
    }

    public Cathedra getCathedra() {
        return cathedra;
    }
    public String getCathedraName() {
        return cathedra.getName();
    }

    public void setCathedra(Cathedra cathedra) {
        this.cathedra = cathedra;
    }

    public List<Vacation> getVacation() {
        return vacation;
    }

    public void setVacation(List<Vacation> vacation) {
        this.vacation = vacation;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Teacher teacher = (Teacher) o;
        return Objects.equals(id, teacher.id) && Objects.equals(subject, teacher.subject) && Objects.equals(cathedra, teacher.cathedra) && Objects.equals(vacation, teacher.vacation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, subject, cathedra, vacation);
    }
}
