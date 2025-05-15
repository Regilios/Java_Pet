package org.example.univer.dao.hibernate;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.example.univer.dao.interfaces.DaoLectureInterface;
import org.example.univer.models.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
@Transactional
@Component
public class HibernateLecture implements DaoLectureInterface {
    private static final Logger logger = LoggerFactory.getLogger(HibernateLecture.class);
    @PersistenceContext
    private EntityManager entityManager;

    @Override
    public void create(Lecture lecture) {
        logger.debug("create lecture {}", lecture);
        entityManager.persist(lecture);
    }

    @Override
    public void update(Lecture lecture) {
        logger.debug("update lecture {}", lecture);
        entityManager.merge(lecture);
    }

    public void deleteById(Long id) {
        Lecture lecture = entityManager.find(Lecture.class, id);
        if (lecture != null) {
            for (Group group : lecture.getGroups()) {
                group.getLections().remove(lecture);
            }
            lecture.getGroups().clear();
            lecture.setTime(null);
            lecture.setTeacher(null);
            lecture.setAudience(null);
            lecture.setSubject(null);
            lecture.setCathedra(null);

            entityManager.remove(lecture);
            logger.debug("Lecture with id {} was deleted", id);
        } else {
            logger.warn("Lecture with id {} not found", id);
        }
    }

    @Override
    public Optional<Lecture> findById(Long id) {
        return Optional.ofNullable(entityManager.createNamedQuery("findLectureWidthGroups", Lecture.class)
                .setParameter("lecture_id", id)
                .getSingleResult());
    }

    @Override
    public List<Lecture> findAll() {
        logger.debug("Find all lecture");
        return entityManager.createNamedQuery("findAllLecture", Lecture.class).getResultList();
    }

    @Override
    public Page<Lecture> findPaginatedLecture(Pageable pageable) {
        Long total = entityManager.createNamedQuery("countAllLecture", Long.class).getSingleResult();
        List<Lecture> lectures = entityManager.createNamedQuery("findAllLecturePaginatedWithGroups", Lecture.class)
                .setFirstResult((int) pageable.getOffset())
                .setMaxResults(pageable.getPageSize())
                .getResultList();
        logger.debug("Found {} lectures. Returning page {} with page size {}",
                lectures.size(),
                pageable.getPageNumber() + 1,
                pageable.getPageSize());

        return new PageImpl<>(lectures, pageable, total);
    }

    @Override
    public boolean isSingle(Lecture lecture) {
        Long result = entityManager.createNamedQuery("countLectureByParam", Long.class)
                .setParameter("teacher_id", lecture.getTeacher())
                .setParameter("subject_id", lecture.getSubject())
                .setParameter("lecture_time_id", lecture.getTime())
                .setParameter("audience_id", lecture.getAudience())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public List<Lecture> getTimetableTeacherForCreate(Teacher teacher, LocalDate localDate) {
        return entityManager.createNamedQuery("getTimetableTeacherForCreate", Lecture.class)
                .setParameter("teacherId", teacher.getId())
                .setParameter("day_l", localDate.getDayOfMonth())
                .setParameter("month_l", localDate.getMonthValue())
                .getResultList();
    }

    @Override
    public List<Lecture> getTimetableTeacherForUpdate(Teacher teacher, Lecture lecture) {
        return entityManager.createNamedQuery("getTimetableTeacherForUpdate", Lecture.class)
                .setParameter("teacherId", teacher.getId())
                .setParameter("timeId", lecture.getTime().getId())
                .setParameter("lectureId", lecture.getId())
                .getResultList();
    }
    @Override
    public List<Lecture> findLecturesByTeacherAndPeriod(Teacher teacher, LocalDate start, LocalDate end) {
        LocalDateTime start_l = start.atStartOfDay();
        LocalDateTime end_l = end.atTime(LocalTime.MAX);
        return entityManager.createNamedQuery("findLecturesByTeacherAndPeriod", Lecture.class)
                .setParameter("start_l", start_l)
                .setParameter("end_l", end_l)
                .setParameter("teacherId", teacher.getId())
                .getResultList();
    }

    @Override
    public boolean findByAudienceDateAndLectureTimeForCreate(Audience audience, LectureTime time) {
        Long result = entityManager.createNamedQuery("findByAudienceDateAndLectureTimeForCreate", Long.class)
                .setParameter("audience_id", audience.getId())
                .setParameter("lecture_time_id", time.getId())
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }

    @Override
    public boolean findByAudienceDateAndLectureTimeForUpdate(Audience audience, LectureTime time, Long id) {
        Long result = entityManager.createNamedQuery("findByAudienceDateAndLectureTimeForUpdate", Long.class)
                .setParameter("audience_id", audience.getId())
                .setParameter("lecture_time_id", time.getId())
                .setParameter("lecture_id", id)
                .getSingleResult();
        return Objects.nonNull(result)  && result > 0;
    }
}
