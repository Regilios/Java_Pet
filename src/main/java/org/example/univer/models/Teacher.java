package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;

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
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
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

}
