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
@Table(name = "lection")
public class Lecture implements Serializable {
    private static final long serialVersionUID = -3903027710562111557L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "lecture_time_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_lecture_time", foreignKeyDefinition =
                    "FOREIGN KEY (lecture_time_id) REFERENCES lectionTime(id) ON DELETE SET NULL"))
    private LectureTime time;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_lecture_teacher", foreignKeyDefinition =
                    "FOREIGN KEY (teacher_id) REFERENCES teacher(id) ON DELETE SET NULL"))
    private Teacher teacher;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "audience_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_lecture_audience", foreignKeyDefinition =
                    "FOREIGN KEY (audience_id) REFERENCES audience(id) ON DELETE SET NULL"))
    private Audience audience;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "subject_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_lecture_subject", foreignKeyDefinition =
                    "FOREIGN KEY (subject_id) REFERENCES subject(id) ON DELETE SET NULL"))
    private Subject subject;

    @ManyToOne(fetch = FetchType.LAZY, optional = true)
    @JoinColumn(name = "cathedra_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_lecture_cathedra", foreignKeyDefinition =
                    "FOREIGN KEY (cathedra_id) REFERENCES cathedra(id) ON DELETE SET NULL"))
    private Cathedra cathedra;

    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "group_lection",
            joinColumns = @JoinColumn(name = "lection_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id"))
    private List<Group> groups = new ArrayList<>();
}
