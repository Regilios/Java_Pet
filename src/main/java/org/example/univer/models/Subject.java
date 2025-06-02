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
    private List<Teacher> teachers = new ArrayList<>();
}
