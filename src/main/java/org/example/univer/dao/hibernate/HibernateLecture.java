package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoLectureInterface;
import org.example.univer.models.*;
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

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Transactional
@Component
public class HibernateLecture implements DaoLectureInterface {

    private static final Logger logger = LoggerFactory.getLogger(HibernateLecture.class);

    @Autowired
    private static SessionFactory sessionFactory;

    @Autowired
    public HibernateLecture(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(Lecture lecture) {
        logger.debug("create lecture {}", lecture);
        Session session = sessionFactory.getCurrentSession();
        session.persist(lecture);
    }

    @Override
    public void update(Lecture lecture) {
        logger.debug("update lecture {}", lecture);
        Session session = sessionFactory.getCurrentSession();
        session.merge(lecture);
    }

    @Override
    public void deleteById(Lecture lecture) {
        logger.debug("Lecture with id {} was deleted", lecture.getId());
        sessionFactory.getCurrentSession().remove(lecture);
    }

    @Override
    public Optional<Lecture> findById(Long id) {
        return Optional.ofNullable(sessionFactory
                .getCurrentSession()
                .createNamedQuery("findLectureWidthGroups", Lecture.class)
                .setParameter("lecture_id", id)
                .getSingleResult());
    }

    @Override
    public List<Lecture> findAll() {
        logger.debug("Find all lecture");
        return sessionFactory.getCurrentSession().createNamedQuery("findAllLecture", Lecture.class).getResultList();
    }

    @Override
    public Page<Lecture> findPaginatedLecture(Pageable pageable) {
        Session session = sessionFactory.getCurrentSession();
        Long total = session.createNamedQuery("countAllLecture", Long.class).uniqueResult();
        List<Lecture> lectures = session.createNamedQuery("findAllLecturePaginatedWithGroups", Lecture.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        logger.debug("Found {} audiences", lectures.size());

        return new PageImpl<>(lectures, pageable, total);
    }

    @Override
    public boolean isSingle(Lecture lecture) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("countLectureByParam", Long.class)
                .setParameter("teacher_id", lecture.getTeacher())
                .setParameter("subject_id", lecture.getSubject())
                .setParameter("lecture_time_id", lecture.getTime())
                .setParameter("audience_id", lecture.getAudience())
                .uniqueResult();
        return result != null && result > 0;
    }

    @Override
    public List<Lecture> getTimetableTeacherForCreate(Teacher teacher, LocalDate localDate) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("getTimetableTeacherForCreate", Lecture.class)
                .setParameter("teacherId", teacher.getId())
                .setParameter("day_l", localDate.getDayOfMonth())
                .setParameter("month_l", localDate.getMonthValue())
                .getResultList();
    }

    @Override
    public List<Lecture> getTimetableTeacherForUpdate(Teacher teacher, LocalDate localDate, Lecture lecture) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("getTimetableTeacherForUpdate", Lecture.class)
                .setParameter("teacherId", teacher.getId())
                .setParameter("day_l", localDate.getDayOfMonth())
                .setParameter("month_l", localDate.getMonthValue())
                .setParameter("lectureId", lecture.getId())
                .getResultList();
    }

    @Override
    public List<Lecture> findLecturesByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        return sessionFactory.getCurrentSession()
                .createNamedQuery("findLecturesByTeacherAndPeriod", Lecture.class)
                .setParameter("start_l", start)
                .setParameter("end_l", end)
                .setParameter("teacherId", teacher.getId())
                .getResultList();
    }

    @Override
    public boolean findByAudienceDateAndLectureTimeForCreate(Audience audience, LectureTime time) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("findByAudienceDateAndLectureTimeForCreate", Long.class)
                .setParameter("audience_id", audience.getId())
                .setParameter("lecture_time_id", time.getId())
                .uniqueResult();
        return result != null && result > 0;
    }

    @Override
    public boolean findByAudienceDateAndLectureTimeForUpdate(Audience audience, LectureTime time, Long id) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("findByAudienceDateAndLectureTimeForUpdate", Long.class)
                .setParameter("audience_id", audience.getId())
                .setParameter("lecture_time_id", time.getId())
                .setParameter("lecture_id", id)
                .uniqueResult();
        return result != null && result > 0;
    }
}
