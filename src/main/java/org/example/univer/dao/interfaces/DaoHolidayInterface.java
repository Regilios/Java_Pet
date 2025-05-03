package org.example.univer.dao.interfaces;

import org.example.univer.models.Holiday;

import java.time.LocalDateTime;

public interface DaoHolidayInterface extends DaoInterfaces<Holiday> {
    boolean isSingle(Holiday holiday);
    boolean lectureDoesNotFallOnHoliday(LocalDateTime date);
}
