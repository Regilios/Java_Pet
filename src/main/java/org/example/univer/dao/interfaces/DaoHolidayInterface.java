package org.example.univer.dao.interfaces;

import org.example.univer.models.Holiday;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

public interface DaoHolidayInterface extends DaoInterfaces<Holiday> {
    boolean isSingle(Holiday holiday);

    boolean lectureDoesNotFallOnHoliday(LocalDateTime date);
}
