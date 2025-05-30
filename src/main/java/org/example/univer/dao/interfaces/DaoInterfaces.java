package org.example.univer.dao.interfaces;

import java.util.List;
import java.util.Optional;

public interface DaoInterfaces<T> {
    void create(T entity);
    void update(T entity);
    void deleteById(Long id);
    Optional<T> findById(Long id);
    List<T> findAll();
}
