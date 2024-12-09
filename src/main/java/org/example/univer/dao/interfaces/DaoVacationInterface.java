package org.example.univer.dao.interfaces;

import org.example.univer.models.Vacation;

public interface DaoVacationInterface extends DaoInterfaces<Vacation> {
    boolean isSingle(Vacation vacation);
}
