package org.example.univer.repositories;

import org.example.univer.models.Holiday;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
@Repository
public interface HolidayRepository extends JpaRepository<Holiday, Long> {
    boolean existsByDescription(String description);

    @Query("SELECT COUNT(h) > 0 FROM Holiday h WHERE :date BETWEEN h.startHoliday AND h.endHoliday")
    boolean existsByDateInHoliday(@Param("date") LocalDate date);
}
