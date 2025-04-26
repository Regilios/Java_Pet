package org.example.univer.dao.hibernate;

import org.example.univer.dao.interfaces.DaoLectureTimeInterface;
import org.example.univer.models.LectureTime;
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
public class HibernateLectureTime implements DaoLectureTimeInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateLectureTime.class);
    @Autowired
    private final SessionFactory sessionFactory;
    @Autowired
    public HibernateLectureTime(SessionFactory sessionFactory) {
        this.sessionFactory = sessionFactory;
    }

    @Override
    public void create(LectureTime lectureTime) {
        logger.debug("create lectureTime {}", lectureTime);
        Session session = sessionFactory.getCurrentSession();
        session.persist(lectureTime);
    }

    @Override
    public void update(LectureTime lectureTime) {
        logger.debug("update lectureTime {}", lectureTime);
        Session session = sessionFactory.getCurrentSession();
        session.merge(lectureTime);
    }

    @Override
    public void deleteById(LectureTime lectureTime) {
        logger.debug("LectureTime with id {} was deleted", lectureTime.getId());
        sessionFactory.getCurrentSession().remove(lectureTime);
    }

    @Override
    public Optional<LectureTime> findById(Long id) {
        logger.debug("Find LectureTime by id: {}", id);
        return Optional.ofNullable(sessionFactory.getCurrentSession().get(LectureTime.class, id));
    }

    @Override
    public List<LectureTime> findAll() {
        logger.debug("Find all LectureTimes");
        return sessionFactory.getCurrentSession().createNamedQuery("findAllLectureTime", LectureTime.class).getResultList();
    }

    @Override
    public boolean isSingle(LectureTime lectureTime) {
        Long result = sessionFactory.getCurrentSession()
                .createNamedQuery("findLectureTime", Long.class)
                .setParameter("start_lection", lectureTime.getStartLocal())
                .setParameter("end_lection", lectureTime.getEndLocal())
                .uniqueResult();
        return result != null && result > 0;
    }
}
