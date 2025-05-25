package org.example.univer.repositories;

import org.example.univer.models.Cathedra;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CathedraRepository extends JpaRepository<Cathedra, Long> {
    boolean existsByName(String name);
}
