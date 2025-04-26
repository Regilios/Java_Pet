package org.example.univer.dao.interfaces;

import java.util.List;

public interface DaoInterfaces<T> {
    void create(T entity);

    void update(T entity);

    void deleteById(Long id);

    T findById(Long id);

    List<T> findAll();
}
