package org.example.univer.models;

import jakarta.persistence.*;
import java.io.Serializable;
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
    public Cathedra() {}

    public Cathedra(Long id, String name) {
        this.id = id;
        this.name = name;

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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cathedra cathedra = (Cathedra) o;
        return Objects.equals(id, cathedra.id) && Objects.equals(name, cathedra.name);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name);
    }
}
