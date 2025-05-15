package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoSubjectInterface;
import org.example.univer.models.Subject;
import org.example.univer.models.Teacher;
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
public class HibernateSubject implements DaoSubjectInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateSubject.class);
    @Autowired
    private final SessionFactory sessionFactory;

    @Autowired
    public HibernateSubject(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Subject subject) {
        logger.debug("create subject {}", subject);
        Session session = sessionFactory.getCurrentSession();
        session.persist(subject);
    }

    @Override
    public void update(Subject subject) {
        logger.debug("update subject {}", subject);
        Session session = sessionFactory.getCurrentSession();
        session.merge(subject);
    }

    @Override
    public void deleteEntity(Subject subject) {
        logger.debug("Subject with id {} was deleted", subject.getId());
        sessionFactory.getCurrentSession().remove(subject);
    }

    @Override
    public Optional<Subject> findById(Long id) {
        logger.debug("Find Subject hibernate by id: {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Subject.class, id));
    }

    @Override
    public List<Subject> findAll() {
        logger.debug("Find all Subject");
        return sessionFactory.getCurrentSession().createNamedQuery("findAllSubjects", Subject.class).getResultList();
    }

    @Override
    public boolean checkTeacherAssignedSubject(Teacher teacher, Subject subject) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("checkTeacherAssignedSubject", Long.class)
                .setParameter("teacher_id", teacher.getId())
                .setParameter("subject_id", subject.getId())
                .uniqueResult();
        return result != null && result > 0;
    }

    @Override
    public boolean isSingle(Subject subject) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("countAllSubjects", Long.class)
                .setParameter("name", subject.getName())
                .uniqueResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public List<Subject> getSubjectById(Long teacher_id) {
        return sessionFactory.getCurrentSession().createNamedQuery("findSubjectsByTeacherId", Subject.class)
                .setParameter("teacher_id", teacher_id)
                .getResultList();
    }
}
