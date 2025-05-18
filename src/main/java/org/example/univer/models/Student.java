package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;

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
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "students")
public class Student extends Person implements Serializable {
    private static final long serialVersionUID = -5326483353908974024L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "group_id", referencedColumnName = "id")
    private Group group;

/*
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
*/

}
