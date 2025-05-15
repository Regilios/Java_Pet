package org.example.univer.dao.interfaces;

import org.example.univer.models.Vacation;

import java.util.List;

public interface DaoVacationInterface extends DaoInterfaces<Vacation> {
    boolean isSingle(Vacation vacation);
    List<Vacation> findByTeacherId(Long id);
}
