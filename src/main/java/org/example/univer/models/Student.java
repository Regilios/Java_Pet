package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

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

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "group_id", referencedColumnName = "id", foreignKey = @ForeignKey(name = "fk_student_group", foreignKeyDefinition = "FOREIGN KEY (group_id) REFERENCES groups(id) ON DELETE SET NULL"))
    private Group group;
}
