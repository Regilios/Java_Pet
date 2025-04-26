package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoVacationInterface;
import org.example.univer.models.Vacation;
import org.hibernate.Hibernate;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Transactional
@Component
public class HibernateVacation implements DaoVacationInterface {

    private static final Logger logger = LoggerFactory.getLogger(HibernateVacation.class);

    @Autowired
    private static SessionFactory sessionFactory;

    @Autowired
    public HibernateVacation(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Vacation vacation) {
        logger.debug("create vacation {}", vacation);
        Session session = sessionFactory.getCurrentSession();
        session.persist(vacation);
    }

    @Override
    public void update(Vacation vacation) {
        logger.debug("update vacation {}", vacation);
        Session session = sessionFactory.getCurrentSession();
        session.merge(vacation);
    }

    @Override
    public void deleteById(Vacation vacation) {
        logger.debug("Vacation with id {} was deleted", vacation.getId());
        sessionFactory.getCurrentSession().remove(vacation);
    }

    @Override
    public Optional<Vacation> findById(Long id) {
        logger.debug("Find Vacation by id: {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Vacation.class, id));
    }

    @Override
    public List<Vacation> findAll() {
        logger.debug("Find all vacation");
        return sessionFactory.getCurrentSession().createNamedQuery("findAllVacation", Vacation.class).getResultList();
    }

    @Override
    public boolean isSingle(Vacation vacation) {
        Long result = sessionFactory.getCurrentSession().createNamedQuery("countVacantion",Long.class)
                .setParameter("startJob",vacation.getStartJob())
                .setParameter("endJob",vacation.getEndJob())
                .setParameter("teacher_id",vacation.getTeacher().getId())
                .uniqueResult();
        return result != null && result > 0;
    }

    @Override
    public List<Vacation> findByTeacherId(Long id) {
        List<Vacation> vacations = sessionFactory.getCurrentSession()
                .createNamedQuery("findListVacantion", Vacation.class)
                .setParameter("teacher_id", id)
                .getResultList();

        for (Vacation vacation : vacations) {
            Hibernate.initialize(vacation.getTeacher());
        }

        return vacations;
    }
}
