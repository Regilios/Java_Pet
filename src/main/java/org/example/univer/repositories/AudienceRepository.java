package org.example.univer.repositories;

import org.example.univer.models.Audience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface AudienceRepository extends JpaRepository<Audience, Long> {
    boolean existsByRoomNumber(Integer roomNumber);
    boolean existsById(Long id);
    Page<Audience> findAllByOrderById(Pageable pageable);
}
