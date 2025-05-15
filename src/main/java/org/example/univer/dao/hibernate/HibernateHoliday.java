package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoHolidayInterface;
import org.example.univer.models.Holiday;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Component
@Transactional
public class HibernateHoliday implements DaoHolidayInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateHoliday.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isSingle(Holiday holiday) {
        Long result = entityManager.createNamedQuery("countHolidayByDescript", Long.class)
                .setParameter("description", holiday.getDescription())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public boolean lectureDoesNotFallOnHoliday(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        Long result = entityManager.createNamedQuery("findHolidayByDate", Long.class)
                .setParameter("date", date)
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public void create(Holiday holiday) {
        logger.debug("create holiday {}", holiday);
        entityManager.persist(holiday);
    }

    @Override
    public void update(Holiday holiday) {
        logger.debug("update holiday {}", holiday);
        entityManager.merge(holiday);
    }

    @Override
    public void deleteById(Long id) {
        Holiday holiday = entityManager.find(Holiday.class, id);
        if (holiday != null) {
            entityManager.remove(holiday);
            logger.debug("Holiday with id {} was deleted", id);
        } else {
            logger.warn("Holiday with id {} not found", id);
        }
    }

    @Override
    public Optional<Holiday> findById(Long id) {
        logger.debug("Find Holiday by id: {}", id);
        return Optional.ofNullable(entityManager.find(Holiday.class, id));
    }

    @Override
    public List<Holiday> findAll() {
        logger.debug("Find all Holiday");
        return entityManager.createNamedQuery("findAllHoliday", Holiday.class).getResultList();

    }
}
