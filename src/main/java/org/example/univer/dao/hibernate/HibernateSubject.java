package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoSubjectInterface;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Component
@Transactional
public class HibernateSubject implements DaoSubjectInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateSubject.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Subject subject) {
        logger.debug("create subject {}", subject);
        entityManager.persist(subject);
    }

    @Override
    public void update(Subject subject) {
        logger.debug("update subject {}", subject);
        entityManager.merge(subject);
    }

    @Override
    public void deleteById(Long id) {
        Subject subject = entityManager.find(Subject.class, id);
        if (subject != null) {
            entityManager.remove(subject);
            logger.debug("Subject with id {} was deleted", id);
        } else {
            logger.warn("Subject with id {} not found", id);
        }
    }

    @Override
    public Optional<Subject> findById(Long id) {
        logger.debug("Find Subject hibernate by id: {}", id);
        return Optional.ofNullable(entityManager.find(Subject.class, id));
    }

    @Override
    public List<Subject> findAll() {
        logger.debug("Find all Subject");
        return entityManager.createNamedQuery("findAllSubjects", Subject.class).getResultList();
    }

    @Override
    public boolean checkTeacherAssignedSubject(Teacher teacher, Subject subject) {
        Long result = entityManager.createNamedQuery("checkTeacherAssignedSubject", Long.class)
                .setParameter("teacher_id", teacher.getId())
                .setParameter("subject_id", subject.getId())
                .getSingleResult();
        return result != null && result > 0;
    }

    @Override
    public boolean isSingle(Subject subject) {
        Long result = entityManager.createNamedQuery("countAllSubjects", Long.class)
                .setParameter("name", subject.getName())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public List<Subject> getSubjectById(Long teacher_id) {
        return entityManager.createNamedQuery("findSubjectsByTeacherId", Subject.class)
                .setParameter("teacher_id", teacher_id)
                .getResultList();
    }
}
