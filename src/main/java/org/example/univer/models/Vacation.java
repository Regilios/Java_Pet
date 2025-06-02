package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;

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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;
}
