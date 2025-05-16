package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoStudentInterface;
import org.example.univer.models.Student;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Component
@Transactional
public class HibernateStudent implements DaoStudentInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateStudent.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Student student) {
        logger.debug("create student {}", student);
        entityManager.persist(student);
    }

    @Override
    public void update(Student student) {
        logger.debug("update student {}", student);
        entityManager.merge(student);
    }

    @Override
    public void deleteById(Long id) {
        Student student = entityManager.find(Student.class, id);
        if (Objects.nonNull(student)) {
            if (Objects.nonNull(student.getGroup())) {
                student.getGroup().getStudents().remove(student);
                student.setGroup(null);
            }
            entityManager.remove(student);
            logger.debug("Student with id {} was deleted", id);
        }
    }

    @Override
    public Optional<Student> findById(Long id) {
        logger.debug("Find Student by id: {}", id);
        return Optional.ofNullable(entityManager.find(Student.class, id));
    }

    @Override
    public List<Student> findAll() {
        logger.debug("Find all students");
        return entityManager.createNamedQuery("findAllStudents", Student.class).getResultList();
    }

    @Override
    public Page<Student> findPaginatedStudents(Pageable pageable) {
        Long total = entityManager.createNamedQuery("countAllStudents", Long.class).getSingleResult();
        List<Student> audiences = entityManager.createNamedQuery("findAllStudentPaginated", Student.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        logger.debug("Found {} students", audiences.size());

        return new PageImpl<>(audiences, pageable, total);
    }

    @Override
    public boolean isSingle(Student student) {
        Long result = entityManager.createNamedQuery("countStudentByName", Long.class)
                .setParameter("firstName", student.getFirstName())
                .setParameter("lastName", student.getLastName())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public Integer checkGroupSize(Student student) {
        Long result = entityManager.createNamedQuery("findStudentsByGroupId", Long.class)
                .setParameter("groupId", student.getGroup().getId())
                .getSingleResult();
       return result.intValue();
    }
}
