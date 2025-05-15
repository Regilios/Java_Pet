package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoAudienceInterface;
import org.example.univer.models.Audience;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Component
@Transactional
public class HibernateAudience implements DaoAudienceInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateAudience.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public List<Audience> findAll() {
        logger.debug("Find all audiences");
        return entityManager.createNamedQuery("findAllAudiences", Audience.class).getResultList();
    }

    @Override
    public void create(Audience audience) {
        logger.debug("create audience {}", audience);
        entityManager.persist(audience);
    }

    @Override
    public void update(Audience audience) {
        logger.debug("update audience {}", audience);
        entityManager.merge(audience);
    }

    @Override
    public void deleteById(Long id) {
        Audience audience = entityManager.find(Audience.class, id);
        if (audience != null) {
            entityManager.remove(audience);
            logger.debug("Audience with id {} was deleted", id);
        } else {
            logger.warn("Audience with id {} not found", id);
        }
    }

    @Override
    public Optional<Audience> findById(Long id) {
        logger.debug("Find audience by id: {}", id);
        return  Optional.ofNullable(entityManager.find(Audience.class, id));
    }

    @Override
    public Page<Audience> findPaginatedAudience(Pageable pageable) {
        Long total = entityManager.createNamedQuery("countAllAudiences", Long.class).getSingleResult();
        List<Audience> audiences = entityManager.createNamedQuery("findAllAudiencesPaginated", Audience.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();

        logger.debug("Found {} audiences. Returning page {} with page size {}",
                audiences.size(),
                pageable.getPageNumber() + 1,
                pageable.getPageSize());

        return new PageImpl<>(audiences, pageable, total);
    }

    @Override
    public boolean isSingle(Audience audience) {
        Long result = entityManager.createNamedQuery("countAudiencesByRoomNumber", Long.class)
                .setParameter("roomNumber", audience.getRoom())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }
}
