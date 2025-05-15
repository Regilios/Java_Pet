package org.example.univer.models;

import jakarta.persistence.*;
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

    public Holiday(Long id, String desc, LocalDate startHoliday, LocalDate endHoliday) {
        this.id = id;
        this.description = desc;
        this.startHoliday = startHoliday;
        this.endHoliday = endHoliday;
    }

    public Holiday() {}

    public LocalDate getStartHoliday() {
        return startHoliday;
    }

    public void setStartHoliday(LocalDate start_holiday) {
        this.startHoliday = start_holiday;
    }

    public String getStartHolidayLocal() {
        return  Objects.nonNull(startHoliday) ? startHoliday.format(DateTimeFormatter.ofPattern(DATE_PATTERN_HOLIDAY)) : null;
    }

    public LocalDate getEndHoliday() {
        return endHoliday;
    }

    public String getEndHolidayLocal() {
        return Objects.nonNull(endHoliday) ? endHoliday.format(DateTimeFormatter.ofPattern(DATE_PATTERN_HOLIDAY)) : null;
    }

    public void setEndHoliday(LocalDate end_holiday) {
        this.endHoliday = end_holiday;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String desc) {
        this.description = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holiday holiday = (Holiday) o;
        return Objects.equals(id, holiday.id) && Objects.equals(description, holiday.description) && Objects.equals(startHoliday, holiday.startHoliday) && Objects.equals(endHoliday, holiday.endHoliday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, startHoliday, endHoliday);
    }
}
