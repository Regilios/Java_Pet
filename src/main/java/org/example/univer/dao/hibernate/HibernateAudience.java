package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoAudienceInterface;
import org.example.univer.models.Audience;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Component
@Transactional
public class HibernateAudience implements DaoAudienceInterface {

    private static final Logger logger = LoggerFactory.getLogger(HibernateAudience.class);
    @Autowired
    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateAudience(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public List<Audience> findAll() {
        logger.debug("Find all audiences");
        // return sessionFactory.getCurrentSession().getNamedQuery("findAllAudiences").getResultList();
        return sessionFactory.getCurrentSession().createNamedQuery("findAllAudiences", Audience.class).getResultList();
    }

    @Override
    public void create(Audience audience) {
        logger.debug("create audience {}", audience);
        Session session = sessionFactory.getCurrentSession();
        // session.save(audience);
        session.persist(audience);
    }

    @Override
    public void update(Audience audience) {
        logger.debug("update audience {}", audience);
        Session session = sessionFactory.getCurrentSession();
        session.merge(audience);
    }

    @Override
    public void deleteById(Audience audience) {
         logger.debug("Audience with id {} was deleted", audience.getId());
         // sessionFactory.getCurrentSession().delete(audience);
         sessionFactory.getCurrentSession().remove(audience);
    }

    @Override
    public Optional<Audience> findById(Long id) {
        logger.debug("Find audience by id: {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Audience.class, id));
    }

    @Override
    public Page<Audience> findPaginatedAudience(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long total = session.createNamedQuery("countAllAudiences", Long.class).uniqueResult();
        List<Audience> audiences = session.createNamedQuery("findAllAudiencesPaginated", Audience.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        logger.debug("Found {} audiences", audiences.size());

        return new PageImpl<>(audiences, pageable, total);
    }

    @Override
    public boolean isSingle(Audience audience) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("countAudiencesByRoomNumber", Long.class)
                .setParameter("roomNumber", audience.getRoom())
                .uniqueResult();
        return result != null && result > 0;
    }
}
