package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoTeacherInterface;
import org.example.univer.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Transactional
@Component
public class HibernateTeacher implements DaoTeacherInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateTeacher.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Teacher teacher) {
        logger.debug("create teacher {}", teacher);
        entityManager.persist(teacher);
    }

    @Override
    public void update(Teacher teacher) {
        logger.debug("update teacher {}", teacher);
        entityManager.merge(teacher);
    }

    @Override
    public void deleteById(Long id) {
        Teacher teacher = entityManager.find(Teacher.class, id);
        if (Objects.nonNull(teacher)) {
            entityManager.remove(teacher);
            logger.debug("Teacher with id {} was deleted", id);
        } else {
            logger.warn("Teacher with id {} not found", id);
        }
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        return Optional.ofNullable(entityManager.find(Teacher.class, id));
    }

    @Override
    public List<Teacher> findAll() {
        logger.debug("Find all teachers");
        return entityManager.createNamedQuery("findAllTeachers", Teacher.class).getResultList();
    }

    @Override
    public boolean isSingle(Teacher teacher) {
        logger.debug("Check teacher is single");
        Long result = entityManager.createNamedQuery("countTeachers",Long.class)
                .setParameter("firstName",teacher.getFirstName())
                .setParameter("lastName",teacher.getLastName())
                .getSingleResult();
        return Objects.nonNull(result) && result > 0;
    }
}
