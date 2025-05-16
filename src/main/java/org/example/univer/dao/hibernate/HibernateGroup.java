package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoGroupInterface;
import org.example.univer.models.Group;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Component
@Transactional
public class HibernateGroup implements DaoGroupInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateGroup.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public boolean isSingle(Group group) {
        Long result = entityManager.createNamedQuery("findGroupByName", Long.class)
                .setParameter("name", group.getName())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public void create(Group group) {
        logger.debug("create groups {}", group);
        entityManager.persist(group);
    }

    @Override
    public void update(Group group) {
        logger.debug("update group {}", group);
        entityManager.merge(group);
    }

    @Override
    public void deleteById(Long id) {
        Group group = entityManager.find(Group.class, id);
        if (Objects.nonNull(group)) {
            entityManager.remove(group);
            logger.debug("Group with id {} was deleted", id);
        } else {
            logger.warn("Group with id {} not found", id);
        }
    }

    @Override
    public Optional<Group> findById(Long id) {
        logger.debug("Find group by id: {}", id);
        return Optional.ofNullable(entityManager.find(Group.class, id));
    }

    @Override
    public List<Group> findAll() {
        logger.debug("Find all groups");
        return entityManager.createNamedQuery("findAllGroups", Group.class).getResultList();
    }

    @Override
    public List<Group> getGroupById(List<Long> groupIds) {
        if (groupIds == null || groupIds.isEmpty()) {
            throw new IllegalArgumentException("The list of groups IDs cannot be null or empty");
        }

        logger.debug("Find groups by IDs: {}", groupIds);

        return entityManager.createNamedQuery("findGroupsByIds", Group.class)
                .setParameter("ids", groupIds)
                .getResultList();
    }
}
