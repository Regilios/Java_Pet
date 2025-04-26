package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoStudentInterface;
import org.example.univer.models.Student;
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
public class HibernateStudent implements DaoStudentInterface {

    private static final Logger logger = LoggerFactory.getLogger(HibernateStudent.class);
    @Autowired
    private final SessionFactory sessionFactory;
    @Autowired
    public HibernateStudent(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Student student) {
        logger.debug("create student {}", student);
        Session session = sessionFactory.getCurrentSession();
        session.persist(student);
    }

    @Override
    public void update(Student student) {
        logger.debug("update student {}", student);
        Session session = sessionFactory.getCurrentSession();
        session.merge(student);
    }

    @Override
    public void deleteById(Student student) {
        logger.debug("Student with id {} was deleted", student.getId());
        sessionFactory.getCurrentSession().remove(student);
    }

    @Override
    public Optional<Student> findById(Long id) {
        logger.debug("Find Student by id: {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(Student.class, id));
    }

    @Override
    public List<Student> findAll() {
        logger.debug("Find all students");
        return sessionFactory.getCurrentSession().createNamedQuery("findAllStudents", Student.class).getResultList();
    }
    @Override
    public Page<Student> findPaginatedStudents(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long total = session.createNamedQuery("countAllStudents", Long.class).uniqueResult();
        List<Student> audiences = session.createNamedQuery("findAllStudentPaginated", Student.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        logger.debug("Found {} students", audiences.size());

        return new PageImpl<>(audiences, pageable, total);
    }


    @Override
    public boolean isSingle(Student student) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("countStudentByName", Long.class)
                .setParameter("firstName", student.getFirstName())
                .setParameter("lastName", student.getLastName())
                .uniqueResult();
        return result != null && result > 0;
    }

    @Override
    public Integer checkGroupSize(Student student) {
        Integer result = sessionFactory.getCurrentSession()
                .createNamedQuery("findStudentsByGroupId", Integer.class)
                .setParameter("group_id", student.getGroup().getId())
                .uniqueResult();
       return result;
    }
}
