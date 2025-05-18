package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;

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
                query = "SELECT l FROM Lecture l JOIN l.time t WHERE l.teacher.id = :teacherId AND EXTRACT(DAY FROM t.startLecture) = :dayLecture AND EXTRACT(MONTH FROM t.startLecture) = :monthLecture"
        ),
        @NamedQuery(
                name = "getTimetableTeacherForUpdate",
                query = "SELECT l FROM Lecture l WHERE l.teacher.id = :teacherId AND l.time.id = :timeId AND l.id <> :lectureId"
        ),
        @NamedQuery(
                name = "findLecturesByTeacherAndPeriod",
                query = "SELECT l FROM Lecture l JOIN l.time t WHERE t.startLecture BETWEEN :startLecture AND :endLecture AND l.teacher.id = :teacherId"
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
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
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


}
