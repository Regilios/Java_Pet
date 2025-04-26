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
                        query = "SELECT COUNT(h) FROM Holiday h WHERE :date BETWEEN h.start_holiday AND h.end_holiday"
                )
        })
@Entity
@Table(name = "holiday")
public class Holiday implements Serializable {
    private static final long serialVersionUID = -3884262256218360709L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column
    private String description;
    @Column(name = "start_holiday", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_holiday;
    @Column(name = "end_holiday", nullable = false)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_holiday;

    public Holiday(Long id, String desc, LocalDate start_holiday, LocalDate end_holiday) {
        this.id = id;
        this.description = desc;
        this.start_holiday = start_holiday;
        this.end_holiday = end_holiday;
    }

    public Holiday() {}

    public LocalDate getStart_holiday() {
        return start_holiday;
    }

    public void setStart_holiday(LocalDate start_holiday) {
        this.start_holiday = start_holiday;
    }

    public String getStartHolidayLocal() {
        return start_holiday != null ? start_holiday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }


    public LocalDate getEnd_holiday() {
        return end_holiday;
    }

    public String getEndHolidayLocal() {
        return end_holiday != null ? end_holiday.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")) : null;
    }

    public void setEnd_holiday(LocalDate end_holiday) {
        this.end_holiday = end_holiday;
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
        return Objects.equals(id, holiday.id) && Objects.equals(description, holiday.description) && Objects.equals(start_holiday, holiday.start_holiday) && Objects.equals(end_holiday, holiday.end_holiday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, description, start_holiday, end_holiday);
    }
}
