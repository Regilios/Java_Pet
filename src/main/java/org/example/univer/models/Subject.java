package org.example.univer.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@NamedQueries({
        @NamedQuery(
                name = "findAllSubjects",
                query = "FROM Subject"
        ),
        @NamedQuery(
                name = "countAllSubjects",
                query = "SELECT COUNT(*) FROM Subject WHERE name=:name"
        ),
        @NamedQuery(
                name = "findSubjectsByTeacherId",
                query = "SELECT s FROM Teacher t JOIN t.subjects s WHERE t.id = :teacher_id"
        ),
        @NamedQuery(
                name = "checkTeacherAssignedSubject",
                query = "SELECT COUNT(t) FROM Teacher t JOIN t.subjects s WHERE t.id = :teacher_id AND s.id = :subject_id"
        )
})

@Entity
@Table(name = "subject")
public class Subject implements Serializable {
    private static final long serialVersionUID = -1045186176540568040L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name")
    private String name;
    @Column(name = "description")
    private String description;
    @ManyToMany(mappedBy = "subjects", cascade = {CascadeType.PERSIST, CascadeType.MERGE})
    private List<Teacher> teacher = new ArrayList<>();

    public Subject(Long id, String name, String description) {
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public Subject() {}

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public List<Teacher> getTeacher() {
        return teacher;
    }

    public void setTeacher(List<Teacher> teacher) {
        this.teacher = teacher;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subject subject = (Subject) o;
        return Objects.equals(id, subject.id) && Objects.equals(name, subject.name) && Objects.equals(description, subject.description) && Objects.equals(teacher, subject.teacher);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, description, teacher);
    }
}
