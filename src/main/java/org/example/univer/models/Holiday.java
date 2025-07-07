package org.example.univer.models;

import com.fasterxml.jackson.annotation.JsonFormat;
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
@Table(name = "holiday")
public class Holiday implements Serializable {
    private static final long serialVersionUID = -3884262256218360709L;
    private static final String DATE_PATTERN_HOLIDAY = "yyyy-MM-dd";
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String description;

    @Column(name = "start_holiday", nullable = false)
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate startHoliday;

    @Column(name = "end_holiday", nullable = false)
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    private LocalDate endHoliday;
}
