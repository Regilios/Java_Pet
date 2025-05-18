package org.example.univer.models;

import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
@NamedQueries({
        @NamedQuery(
                name = "findAllVacation",
                query = "FROM Vacation"
        ),
        @NamedQuery(
                name = "countVacantion",
                query = "SELECT COUNT(v) FROM Vacation v WHERE v.startJob =:startJob AND v.endJob =:endJob AND v.teacher.id =:teacher_id"
        ),
        @NamedQuery(
                name =  "findListVacantion",
                query = "FROM Vacation v WHERE v.teacher.id = :teacher_id"
        ),
        @NamedQuery(
                name =  "findVacantionWithTeacher",
                query = "SELECT v FROM Vacation v JOIN FETCH v.teacher WHERE v.id = :vacId"
        )

})
@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "vacation")
public class Vacation implements Serializable {
    private static final long serialVersionUID = -2595365395005712101L;
    private static final String DATE_PATTERN_VACATION = "yyyy-MM-dd";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "startjob", nullable = false)
    @DateTimeFormat(pattern = DATE_PATTERN_VACATION)
    private LocalDate startJob;

    @Column(name = "endjob", nullable = false)
    @DateTimeFormat(pattern = DATE_PATTERN_VACATION)
    private LocalDate endJob;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;

/*
    public String getStartJobString() {
        return Objects.nonNull(startJob) ? startJob.format(DateTimeFormatter.ofPattern(DATE_PATTERN_VACATION)) : null;
    }
    public String getEndJobString() {
        return Objects.nonNull(endJob) ? endJob.format(DateTimeFormatter.ofPattern(DATE_PATTERN_VACATION)) : null;
    }
*/
}
