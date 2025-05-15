package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoCathedraInterface;
import org.example.univer.models.Cathedra;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Component
@Transactional
public class HibernateCathedra implements DaoCathedraInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateCathedra.class);
    @Autowired
    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateCathedra(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean isSingle(Cathedra cathedra) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("findCathedraByName", Long.class)
                .setParameter("name", cathedra.getName())
                .uniqueResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public void create(Cathedra cathedra) {
        logger.debug("create cathedra {}", cathedra);
        Session session = sessionFactory.getCurrentSession();
        session.persist(cathedra);
    }

    @Override
    public void update(Cathedra cathedra) {
        logger.debug("update cathedra {}", cathedra);
        Session session = sessionFactory.getCurrentSession();
        session.merge(cathedra);
    }

    @Override
    public void deleteEntity(Cathedra cathedra) {
        logger.debug("Cathedra with id {} was deleted", cathedra.getId());
        sessionFactory.getCurrentSession().remove(cathedra);
    }

    @Override
    public Optional<Cathedra> findById(Long id) {
        logger.debug("Find Cathedra by id: {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Cathedra.class, id));
    }

    @Override
    public List<Cathedra> findAll() {
        logger.debug("Find all cathedrals");
        return sessionFactory.getCurrentSession().createNamedQuery("findAllCathedras", Cathedra.class).getResultList();
    }
}
