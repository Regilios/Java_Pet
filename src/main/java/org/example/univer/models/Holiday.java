package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.format.annotation.DateTimeFormat;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
@NamedQueries(
        {
                @NamedQuery(
                        name = "findAllHoliday",
                        query = "FROM Holiday"
                ),
                @NamedQuery(
                        name = "findHolidayByName",
                        query = "SELECT COUNT(*) FROM Holiday WHERE description=:desc"
                ),
                @NamedQuery(
                        name = "countHolidayByDescript",
                        query = "SELECT COUNT(*) FROM Holiday WHERE description = :description"
                ),
                @NamedQuery(
                        name = "findHolidayByDate",
                        query = "SELECT COUNT(h) FROM Holiday h WHERE :date BETWEEN h.startHoliday AND h.endHoliday"
                )
        })
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
    private LocalDate startHoliday;
    @Column(name = "end_holiday", nullable = false)
    @DateTimeFormat(pattern = DATE_PATTERN_HOLIDAY)
    private LocalDate endHoliday;
/*
    public String getStartHolidayLocal() {
        return  Objects.nonNull(startHoliday) ? startHoliday.format(DateTimeFormatter.ofPattern(DATE_PATTERN_HOLIDAY)) : null;
    }

    public String getEndHolidayLocal() {
        return Objects.nonNull(endHoliday) ? endHoliday.format(DateTimeFormatter.ofPattern(DATE_PATTERN_HOLIDAY)) : null;
    }
*/
}
