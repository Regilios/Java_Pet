package org.example.univer.repositories;

import org.example.univer.models.*;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
class LectureRepositoryTest {

    @Autowired
    private LectureRepository lectureRepository;

    @Autowired
    private TestEntityManager entityManager;

    @Test
    void whenFindAllLectures_thenReturnPagedResult() {
        Lecture lecture = createLecture();
        persistDependencies(lecture);
        entityManager.persist(lecture);
        entityManager.flush();

        Page<Lecture> result = lectureRepository.findAllLectures(PageRequest.of(0, 10));

        assertThat(result.getContent()).hasSize(1);
        assertThat(result.getContent().get(0)).isEqualTo(lecture);
    }

    @Test
    void whenFindWithGroupsByIdIn_thenGroupsAreFetched() {
        Lecture lecture = createLecture();
        Group group = new Group();
        group.setName("Group A");
        group.setCathedra(lecture.getCathedra());

        lecture.setGroups(List.of(group));
        group.setLectures(List.of(lecture));

        persistDependencies(lecture);
        entityManager.persist(group);
        entityManager.persist(lecture);
        entityManager.flush();

        List<Lecture> lectures = lectureRepository.findWithGroupsByIdIn(List.of(lecture.getId()));

        assertThat(lectures).hasSize(1);
        assertThat(lectures.get(0).getGroups()).hasSize(1);
        assertThat(lectures.get(0).getGroups().get(0).getName()).isEqualTo("Group A");
    }

    @Test
    void whenExistsByTeacherIdAndSubjectIdAndTimeIdAndAudienceId_thenReturnTrue() {
        Lecture lecture = createLecture();
        persistDependencies(lecture);
        entityManager.persist(lecture);
        entityManager.flush();

        boolean exists = lectureRepository.existsByTeacherIdAndSubjectIdAndTimeIdAndAudienceId(
                lecture.getTeacher().getId(),
                lecture.getSubject().getId(),
                lecture.getTime().getId(),
                lecture.getAudience().getId());

        assertThat(exists).isTrue();
    }

    @Test
    void whenLectureWithSameTeacherAndTimeExists_thenGetTimetableTeacherForUpdateReturnsIt() {

        Lecture lecture1 = createLecture();
        persistDependencies(lecture1);
        entityManager.persist(lecture1);
        entityManager.flush();

        Lecture lecture2 = createLecture();
        lecture2.setTime(lecture1.getTime());
        lecture2.setTeacher(lecture1.getTeacher());
        lecture2.setAudience(lecture1.getAudience());
        lecture2.setSubject(lecture1.getSubject());
        lecture2.setCathedra(lecture1.getCathedra());
        entityManager.persist(lecture2);
        entityManager.flush();

        List<Lecture> result = lectureRepository.getTimetableTeacherForUpdate(
                lecture2.getTeacher().getId(),
                lecture2.getTime().getId(),
                lecture2.getId()
        );

        assertThat(result)
                .hasSize(1)
                .extracting("id")
                .containsExactly(lecture1.getId());
    }

    private Lecture createLecture() {
        Cathedra cathedra = new Cathedra();
        cathedra.setName("Math");

        Subject subject = new Subject();
        subject.setName("Algebra");
        subject.setDescription("Desc");

        Audience audience = new Audience();
        audience.setRoomNumber(101);
        audience.setCapacity(30);

        LectureTime time = new LectureTime();
        time.setStartLecture(LocalDateTime.of(2025, 5, 1, 10, 0));
        time.setEndLecture(LocalDateTime.of(2025, 5, 1, 12, 0));

        Teacher teacher = new Teacher();
        teacher.setFirstName("John");
        teacher.setLastName("Doe");
        teacher.setBirthday(LocalDate.of(1980, 1, 1));
        teacher.setEmail("john@example.com");
        teacher.setPhone("1234567890");
        teacher.setAddress("Street");
        teacher.setGender(Gender.MALE);
        teacher.setCathedra(cathedra);
        teacher.setSubjects(List.of(subject));

        Lecture lecture = new Lecture();
        lecture.setCathedra(cathedra);
        lecture.setSubject(subject);
        lecture.setTime(time);
        lecture.setAudience(audience);
        lecture.setTeacher(teacher);

        return lecture;
    }

    private void persistDependencies(Lecture lecture) {
        entityManager.persist(lecture.getTeacher().getCathedra());
        lecture.getTeacher().getSubjects().forEach(entityManager::persist);
        entityManager.persist(lecture.getTeacher());
        entityManager.persist(lecture.getTime());
        entityManager.persist(lecture.getAudience());
        entityManager.persist(lecture.getSubject());
        entityManager.persist(lecture.getCathedra());
    }
}
