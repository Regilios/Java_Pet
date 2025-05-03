package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoTeacherInterface;
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
@Transactional
@Component
public class HibernateTeacher implements DaoTeacherInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateTeacher.class);

    @Autowired
    private static SessionFactory sessionFactory;

    @Autowired
    public HibernateTeacher(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Teacher teacher) {
        logger.debug("create teacher {}", teacher);
        Session session = sessionFactory.getCurrentSession();
        session.persist(teacher);
    }

    @Override
    public void update(Teacher teacher) {
        logger.debug("update teacher {}", teacher);
        Session session = sessionFactory.getCurrentSession();
        session.merge(teacher);
    }

    @Override
    public void deleteEntity(Teacher teacher) {
        logger.debug("Teacher with id {} was deleted", teacher.getId());
        sessionFactory.getCurrentSession().remove(teacher);
    }

    @Override
    public Optional<Teacher> findById(Long id) {
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Teacher.class, id));
    }

    @Override
    public List<Teacher> findAll() {
        logger.debug("Find all teachers");
        return sessionFactory.getCurrentSession().createNamedQuery("findAllTeachers", Teacher.class).getResultList();
    }

    @Override
    public boolean isSingle(Teacher teacher) {
        logger.debug("Check teacher is single");
        Long result = sessionFactory.getCurrentSession().createNamedQuery("countTeachers",Long.class)
                .setParameter("firstName",teacher.getFirstName())
                .setParameter("lastName",teacher.getLastName())
                .uniqueResult();
        return Objects.nonNull(result)  && result > 0;
    }
}
