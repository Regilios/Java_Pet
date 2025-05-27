package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cathedra_id", referencedColumnName = "id")
    private Cathedra cathedra;

    @OneToMany(mappedBy = "group", fetch = FetchType.LAZY)
    private List<Student> students = new ArrayList<>();

    @ManyToMany(mappedBy = "groups", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.LAZY)
    private List<Lecture> lectures = new ArrayList<>();
}
