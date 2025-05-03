package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoHolidayInterface;
import org.example.univer.models.Holiday;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateHoliday(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean isSingle(Holiday holiday) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("countHolidayByDescript", Long.class)
                .setParameter("description", holiday.getDescription())
                .uniqueResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public boolean lectureDoesNotFallOnHoliday(LocalDateTime dateTime) {
        LocalDate date = dateTime.toLocalDate();
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("findHolidayByDate", Long.class)
                .setParameter("date", date)
                .uniqueResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public void create(Holiday holiday) {
        logger.debug("create holiday {}", holiday);
        Session session = sessionFactory.getCurrentSession();
        session.persist(holiday);
    }

    @Override
    public void update(Holiday holiday) {
        logger.debug("update holiday {}", holiday);
        Session session = sessionFactory.getCurrentSession();
        session.merge(holiday);
    }

    @Override
    public void deleteEntity(Holiday holiday) {
        logger.debug("Audience with id {} was deleted", holiday.getId());
        sessionFactory.getCurrentSession().remove(holiday);
    }

    @Override
    public Optional<Holiday> findById(Long id) {
        logger.debug("Find Holiday by id: {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Holiday.class, id));
    }

    @Override
    public List<Holiday> findAll() {
        logger.debug("Find all Holiday");
        return sessionFactory.getCurrentSession().createNamedQuery("findAllHoliday", Holiday.class).getResultList();

    }
}
