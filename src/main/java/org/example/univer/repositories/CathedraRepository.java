package org.example.univer.repositories;

import org.example.univer.models.Cathedra;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface CathedraRepository extends JpaRepository<Cathedra, Long> {
    boolean existsByName(String name);
}
