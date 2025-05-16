package org.example.univer.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
@NamedQueries({
        @NamedQuery(
                name = "findAllLecture",
                query = "FROM Lecture"
        ),
        @NamedQuery(
                name = "countAllLecture",
                query = "SELECT COUNT(a) FROM Lecture a"
        ),
        @NamedQuery(
                name = "findAllLecturePaginatedWithGroups",
                query = "SELECT DISTINCT l FROM Lecture l LEFT JOIN FETCH l.groups g ORDER BY l.id"
        ),
        @NamedQuery(
                name = "countLectureByParam",
                query = "SELECT COUNT(*) FROM Lecture WHERE teacher = :teacherId AND subject = :subjectId AND time = :lectureTimeId  AND audience = :audienceId"
        ),
        @NamedQuery(
                name = "findLectureWidthGroups",
                query = "SELECT DISTINCT l FROM Lecture l LEFT JOIN FETCH l.groups g WHERE l.id = :lecture_id"
        ),
        @NamedQuery(
                name = "getTimetableTeacherForCreate",
                query = "SELECT l FROM Lecture l JOIN l.time t WHERE l.teacher.id = :teacherId AND EXTRACT(DAY FROM t.startLection) = :dayLecture AND EXTRACT(MONTH FROM t.startLection) = :monthLecture"
        ),
        @NamedQuery(
                name = "getTimetableTeacherForUpdate",
                query = "SELECT l FROM Lecture l WHERE l.teacher.id = :teacherId AND l.time.id = :timeId AND l.id <> :lectureId"
        ),
        @NamedQuery(
                name = "findLecturesByTeacherAndPeriod",
                query = "SELECT l FROM Lecture l JOIN l.time t WHERE t.startLection BETWEEN :startLecture AND :endLecture AND l.teacher.id = :teacherId"
        ),
        @NamedQuery(
                name = "findByAudienceDateAndLectureTimeForCreate",
                query = "SELECT COUNT(l) FROM Lecture l JOIN l.audience a JOIN l.time t WHERE t.id = :lectureTimeId AND  a.id = :audienceId"
        ),
        @NamedQuery(
                name = "findByAudienceDateAndLectureTimeForUpdate",
                query = "SELECT COUNT(l) FROM Lecture l JOIN l.audience a JOIN l.time t WHERE t.id = :lectureTimeId AND  a.id = :audienceId AND l.id = :lectureId AND l.id <> :lectureId"
        )
 })
@Entity
@Table(name = "lection")
public class Lecture implements Serializable {
    private static final long serialVersionUID = -3903027710562111557L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "lecture_time_id", referencedColumnName = "id")
    private LectureTime time;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "teacher_id", referencedColumnName = "id")
    private Teacher teacher;
    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "group_lection",
            joinColumns = @JoinColumn(name = "lection_id"),
            inverseJoinColumns = @JoinColumn(name = "group_id")
    )
    private List<Group> groups = new ArrayList<>();
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "audience_id", referencedColumnName = "id")
    private Audience audience;

    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "subject_id", referencedColumnName = "id")
    private Subject subject;
    @ManyToOne(fetch = FetchType.EAGER)
    @JoinColumn(name = "cathedra_id", referencedColumnName = "id")
    private Cathedra cathedra;

    public Lecture(Long id, Cathedra cathedra, Teacher teacher, Subject subject, LectureTime time, Audience audience, List<Group> group) {
        this.id = id;
        this.time = time;
        this.teacher = teacher;
        this.cathedra = cathedra;
        this.audience = audience;
        this.subject = subject;
        this.groups = group;
    }

    public Lecture() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LectureTime getTime() {
        return time;
    }

    public void setTime(LectureTime time) {
        this.time = time;
    }

    public Teacher getTeacher() {
        return teacher;
    }

    public void setTeacher(Teacher teacher) {
        this.teacher = teacher;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public Audience getAudience() {
        return audience;
    }

    public void setAudience(Audience audience) {
        this.audience = audience;
    }

    public Subject getSubject() {
        return subject;
    }

    public void setSubject(Subject subject) {
        this.subject = subject;
    }

    public Cathedra getCathedra() {
        return cathedra;
    }

    public void setCathedra(Cathedra cathedra) {
        this.cathedra = cathedra;
    }

    public String getTimeStart() {
        return time.getStartLection();
    }

    public LocalDateTime getLocalTimeStart() {
        return time.getStartLocal();
    }

    public String getTimeEnd() {
        return time.getEndLection();
    }

    public LocalDateTime getLocalTimeEnd() {
        return time.getEndLocal();
    }

    public String getTeacherFirstName() {
        return teacher.getFirstName();
    }

    public String getTeacherLastName() {
        return teacher.getLastName();
    }

    public String getCathedraName() {
        return cathedra.getName();
    }

    public String getAudienceRoom() {
        return audience.getRoomString();
    }

    public String getSubjectName() {
        return subject.getName();
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Lecture lecture = (Lecture) o;
        return Objects.equals(id, lecture.id) && Objects.equals(time, lecture.time) && Objects.equals(teacher, lecture.teacher) && Objects.equals(groups, lecture.groups) && Objects.equals(audience, lecture.audience) && Objects.equals(subject, lecture.subject) && Objects.equals(cathedra, lecture.cathedra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, teacher, groups, audience, subject, cathedra);
    }
}
