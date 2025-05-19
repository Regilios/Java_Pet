package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;

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
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "cathedra")
public class Cathedra implements Serializable {
    private static final long serialVersionUID = -1911433105376550879L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "name", nullable = false)
    private String name;
}
