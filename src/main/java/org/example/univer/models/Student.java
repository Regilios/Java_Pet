package org.example.univer.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;
@NamedQueries({
        @NamedQuery(
                name = "findAllStudents",
                query = "FROM Student"
        ),
        @NamedQuery(
                name = "countStudentByName",
                query = "SELECT COUNT(*) FROM Student WHERE firstName = :firstName AND lastName = :lastName"
        ),
        @NamedQuery(
                name = "countAllStudents",
                query = "SELECT COUNT(a) FROM Student a"
        ),
        @NamedQuery(
                name = "findAllStudentPaginated",
                query = "FROM Student ORDER BY id"
        ),
        @NamedQuery(
                name = "findStudentsByGroupId",
                query = "SELECT COUNT(s) FROM Student s WHERE s.group.id = :groupId"
        )
})
@Entity
@Table(name = "students")
public class Student extends Person implements Serializable {
    private static final long serialVersionUID = -5326483353908974024L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

    public Student(Long id, String firstName, String lastName, Gender gender, String address, String email, String phone, LocalDate birthday, Group group) {
        super(firstName, lastName, gender, address, email, phone, birthday);
        this.id = id;
        this.group = group;
    }

    public Student() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public String getGroupName() {
        return group.getName();
    }

    public void setGroup(Group group) {
        this.group = group;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) &&
                Objects.equals(firstName, student.firstName) &&
                Objects.equals(lastName, student.lastName) &&
                Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, group);
    }
}
