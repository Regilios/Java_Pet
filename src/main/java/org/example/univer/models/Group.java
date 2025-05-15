package org.example.univer.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
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
    @OneToMany(mappedBy = "group", cascade = CascadeType.ALL, fetch = FetchType.EAGER)
    private List<Student> students = new ArrayList<>();
    @ManyToMany(mappedBy = "groups", cascade = {CascadeType.PERSIST, CascadeType.MERGE}, fetch = FetchType.EAGER)
    private List<Lecture> lections = new ArrayList<>();

    public Group(String name, Cathedra cathedra) {
        this.name = name;
        this.cathedra = cathedra;
    }

    public Group() {}

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

    public Cathedra getCathedra() {
        return cathedra;
    }

    public void setCathedra(Cathedra cathedra) {
        this.cathedra = cathedra;
    }

    public String getCathedraName() {
        return cathedra.getName();
    }

    public List<Student> getStudents() {
        return students;
    }

    public void setStudents(List<Student> students) {
        this.students = students;
    }

    public List<Lecture> getLections() {
        return lections;
    }

    public void setLections(List<Lecture> lections) {
        this.lections = lections;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Group group = (Group) o;
        return Objects.equals(id, group.id) && Objects.equals(name, group.name) && Objects.equals(cathedra, group.cathedra) && Objects.equals(students, group.students);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, cathedra, students);
    }
}
