package org.example.univer.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@NamedQueries(
    {
            @NamedQuery(
                    name = "findAllCathedras",
                    query = "FROM Cathedra"
            ),
            @NamedQuery(
                    name = "findCathedraByName",
                    query = "SELECT COUNT(*) FROM Cathedra WHERE name=:name"
            )
    })
@Entity
@Table(name = "cathedra")
public class Cathedra implements Serializable {
    private static final long serialVersionUID = -1911433105376550879L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "name", nullable = false)
    private String name;
    @OneToMany(mappedBy = "cathedra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Group> groups = new ArrayList<>();
    @OneToMany(mappedBy = "cathedra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Teacher> teachers = new ArrayList<>();
    @OneToMany(mappedBy = "cathedra", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Lecture> leactions = new ArrayList<>();

    public Cathedra() {}

    public Cathedra(Long id, String name, List<Group> groups, List<Teacher> teachers, List<Lecture> leactions) {
        this.id = id;
        this.name = name;
        this.groups = groups;
        this.teachers = teachers;
        this.leactions = leactions;
    }

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

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Lecture> getLeactions() {
        return leactions;
    }

    public void setLeactions(List<Lecture> leactions) {
        this.leactions = leactions;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cathedra cathedra = (Cathedra) o;
        return Objects.equals(id, cathedra.id) && Objects.equals(name, cathedra.name) && Objects.equals(groups, cathedra.groups) && Objects.equals(teachers, cathedra.teachers) && Objects.equals(leactions, cathedra.leactions);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, groups, teachers, leactions);
    }
}
