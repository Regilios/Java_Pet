package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoGroupInterface;
import org.example.univer.models.Group;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
@Component
@Transactional
public class HibernateGroup implements DaoGroupInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateGroup.class);
    @Autowired
    private final SessionFactory sessionFactory;
    @Autowired
    public HibernateGroup(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public boolean isSingle(Group group) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("findGroupByName", Long.class)
                .setParameter("name", group.getName())
                .uniqueResult();
        return result != null && result > 0;
    }

    @Override
    public void create(Group group) {
        logger.debug("create groups {}", group);
        Session session = sessionFactory.getCurrentSession();
        session.persist(group);
    }

    @Override
    public void update(Group group) {
        logger.debug("update group {}", group);
        Session session = sessionFactory.getCurrentSession();
        session.merge(group);
    }

    @Override
    public void deleteById(Group group) {
        logger.debug("Group with id {} was deleted", group.getId());
        sessionFactory.getCurrentSession().remove(group);
    }

    @Override
    public Optional<Group> findById(Long id) {
        logger.debug("Find group by id: {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Group.class, id));
    }

    @Override
    public List<Group> findAll() {
        logger.debug("Find all groups");
        return sessionFactory.getCurrentSession().createNamedQuery("findAllGroups", Group.class).getResultList();
    }

    @Override
    public List<Group> getGroupById(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            throw new IllegalArgumentException("The list of groups IDs cannot be null or empty");
        }

        logger.debug("Find groups by IDs: {}", groupIds);

        return sessionFactory.getCurrentSession()
                .createNamedQuery("findGroupsByIds", Group.class)
                .setParameterList("ids", groupIds)
                .getResultList();
    }
}
