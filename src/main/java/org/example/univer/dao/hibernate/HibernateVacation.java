package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoVacationInterface;
import org.example.univer.models.Vacation;
import org.hibernate.Hibernate;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Transactional
@Component
public class HibernateVacation implements DaoVacationInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateVacation.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Vacation vacation) {
        logger.debug("create vacation {}", vacation);
        entityManager.persist(vacation);
    }

    @Override
    public void update(Vacation vacation) {
        logger.debug("update vacation {}", vacation);
        entityManager.merge(vacation);
    }

    @Override
    public void deleteById(Long id) {
        Vacation vacation = entityManager.find(Vacation.class, id);
        if (vacation != null) {
            entityManager.remove(vacation);
            logger.debug("Vacation with id {} was deleted", id);
        } else {
            logger.warn("Vacation with id {} not found", id);
        }
    }

    @Override
    public Optional<Vacation> findById(Long id) {
        logger.debug("Find Vacation by id: {}", id);
        return Optional.ofNullable(entityManager.createNamedQuery("findVacantionWithTeacher", Vacation.class)
                .setParameter("vacId", id)
                .getSingleResult());
    }

    @Override
    public List<Vacation> findAll() {
        logger.debug("Find all vacation");
        return entityManager.createNamedQuery("findAllVacation", Vacation.class).getResultList();
    }

    @Override
    public boolean isSingle(Vacation vacation) {
        Long result = entityManager.createNamedQuery("countVacantion",Long.class)
                .setParameter("startJob",vacation.getStartJob())
                .setParameter("endJob",vacation.getEndJob())
                .setParameter("teacher_id",vacation.getTeacher().getId())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public List<Vacation> findByTeacherId(Long id) {
        List<Vacation> vacations = entityManager.createNamedQuery("findListVacantion", Vacation.class)
                .setParameter("teacher_id", id)
                .getResultList();

        for (Vacation vacation : vacations) {
            Hibernate.initialize(vacation.getTeacher());
        }

        return vacations;
    }
}
