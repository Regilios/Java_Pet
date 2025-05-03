package org.example.univer.dao.interfaces;

import org.example.univer.models.Audience;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface DaoAudienceInterface extends DaoInterfaces<Audience>{
    Page<Audience> findPaginatedAudience(Pageable pageable);
    boolean isSingle(Audience audience);
}
