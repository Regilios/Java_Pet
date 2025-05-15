package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoLectureTimeInterface;
import org.example.univer.models.LectureTime;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Component
@Transactional
public class HibernateLectureTime implements DaoLectureTimeInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateLectureTime.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(LectureTime lectureTime) {
        logger.debug("create lectureTime {}", lectureTime);
        entityManager.persist(lectureTime);
    }

    @Override
    public void update(LectureTime lectureTime) {
        logger.debug("update lectureTime {}", lectureTime);
        entityManager.merge(lectureTime);
    }

    @Override
    public void deleteById(Long id) {
        LectureTime lectureTime = entityManager.find(LectureTime.class, id);
        if (lectureTime != null) {
            entityManager.remove(lectureTime);
            logger.debug("LectureTime with id {} was deleted", id);
        } else {
            logger.warn("LectureTime with id {} not found", id);
        }
    }

    @Override
    public Optional<LectureTime> findById(Long id) {
        logger.debug("Find LectureTime by id: {}", id);
        return Optional.ofNullable(entityManager.find(LectureTime.class, id));
    }

    @Override
    public List<LectureTime> findAll() {
        logger.debug("Find all LectureTimes");
        return entityManager.createNamedQuery("findAllLectureTime", LectureTime.class).getResultList();
    }

    @Override
    public boolean isSingle(LectureTime lectureTime) {
        Long result = entityManager.createNamedQuery("findLectureTime", Long.class)
                .setParameter("start_lection", lectureTime.getStartLocal())
                .setParameter("end_lection", lectureTime.getEndLocal())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }
}
