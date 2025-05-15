package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoCathedraInterface;
import org.example.univer.models.Cathedra;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Component
@Transactional
public class HibernateCathedra implements DaoCathedraInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateCathedra.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isSingle(Cathedra cathedra) {
        Long result = entityManager.createNamedQuery("findCathedraByName", Long.class)
                .setParameter("name", cathedra.getName())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public void create(Cathedra cathedra) {
        logger.debug("create cathedra {}", cathedra);
        entityManager.persist(cathedra);
    }

    @Override
    public void update(Cathedra cathedra) {
        logger.debug("update cathedra {}", cathedra);
        entityManager.merge(cathedra);
    }

    @Override
    public void deleteById(Long id) {
        Cathedra cathedra = entityManager.find(Cathedra.class, id);
        if (cathedra != null) {
            entityManager.remove(cathedra);
            logger.debug("Cathedra with id {} was deleted", id);
        } else {
            logger.warn("Cathedra with id {} not found", id);
        }
    }

    @Override
    public Optional<Cathedra> findById(Long id) {
        logger.debug("Find Cathedra by id: {}", id);
        return Optional.ofNullable(entityManager.find(Cathedra.class, id));
    }

    @Override
    public List<Cathedra> findAll() {
        logger.debug("Find all cathedrals");
        return entityManager.createNamedQuery("findAllCathedras", Cathedra.class).getResultList();
    }
}
