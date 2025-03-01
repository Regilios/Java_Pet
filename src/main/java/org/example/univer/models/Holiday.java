package org.example.univer.models;

import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Objects;

public class Holiday implements Serializable {
    private static final long serialVersionUID = 3884262256218360709L;
    private Long id;
    private String desc;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate start_holiday;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate end_holiday;
    private DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd MMMM yyyy");

    public Holiday(Long id, String desc, LocalDate start_holiday, LocalDate end_holiday) {
        this.id = id;
        this.desc = desc;
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
        return start_holiday.format(formatter);
    }


    public LocalDate getEnd_holiday() {
        return end_holiday;
    }

    public String getEndHolidayLocal() {
        return end_holiday.format(formatter);
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

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Holiday holiday = (Holiday) o;
        return Objects.equals(id, holiday.id) && Objects.equals(desc, holiday.desc) && Objects.equals(start_holiday, holiday.start_holiday) && Objects.equals(end_holiday, holiday.end_holiday);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, desc, start_holiday, end_holiday);
    }
}
