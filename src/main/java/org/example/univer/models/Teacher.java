package org.example.univer.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@NamedQueries({
        @NamedQuery(
                name = "findAllTeachers",
                query = "FROM Teacher"
        ),
        @NamedQuery(
                name = "countTeachers",
                query = "SELECT COUNT(t) FROM Teacher t WHERE t.firstName =:firstName AND t.lastName =:lastName"
        )
})
@Entity
@Table(name = "teacher")
public class Teacher extends Person implements Serializable {
    private static final long serialVersionUID = -2167704875913420388L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "teacher_subject",
            joinColumns = @JoinColumn(name = "teacher_id"),
            inverseJoinColumns = @JoinColumn(name = "subject_id")
    )
    private List<Subject> subjects = new ArrayList<>();

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cathedra_id", referencedColumnName = "id")
    private Cathedra cathedra;

    @OneToMany(mappedBy = "teacher", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Vacation> vacation = new ArrayList<>();

    public Teacher(String firstName, String lastName, Gender gender, String address, String email, String phone, LocalDate birthday, Long id, Cathedra cathedra, List<Subject> subjects, List<Vacation> vacation) {
        super(firstName, lastName, gender, address, email, phone, birthday);
        this.id = id;
        this.cathedra = cathedra;
        this.subjects = subjects;
        this.vacation = vacation;
    }

    public Teacher() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public List<Subject> getSubjects() {
        return subjects;
    }

    public void setSubjects(List<Subject> subject) {
        this.subjects = subject;
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
        return Objects.equals(id, teacher.id) && Objects.equals(subjects, teacher.subjects) && Objects.equals(cathedra, teacher.cathedra) && Objects.equals(vacation, teacher.vacation);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, subjects, cathedra, vacation);
    }
}
