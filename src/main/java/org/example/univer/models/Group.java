package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

@NamedQueries(
        {
                @NamedQuery(
                        name = "findAllGroups",
                        query = "FROM Group"
                ),
                @NamedQuery(
                        name = "findGroupByName",
                        query = "SELECT COUNT(*) FROM Group WHERE name=:name"
                ),
                @NamedQuery(
                        name = "findGroupsByIds",
                        query = "SELECT g FROM Group g JOIN FETCH g.cathedra c WHERE g.id IN (:ids)"
                )
        })
@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "groups")
public class Group implements Serializable {
    private static final long serialVersionUID = -8713721246554669520L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cathedra_id", referencedColumnName = "id")
    private Cathedra cathedra;

    @OneToMany(mappedBy = "group", cascade = { CascadeType.PERSIST, CascadeType.MERGE }, fetch = FetchType.EAGER)
    private List<Student> students = new ArrayList<>();

    @ManyToMany(mappedBy = "groups", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Lecture> lectures = new ArrayList<>();
}
