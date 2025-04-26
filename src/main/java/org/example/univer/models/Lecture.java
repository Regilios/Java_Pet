package org.example.univer.models;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Lecture implements Serializable {
    private static final long serialVersionUID = 3903027710562111557L;
    private Long id;
    private LectureTime time;
    private Teacher teacher;
    private List<Group> group = new ArrayList<>();
    private Audience audience;
    private Subject subject;
    private Cathedra cathedra;

    public Lecture(Long id, Cathedra cathedra, Teacher teacher, Subject subject, LectureTime time, Audience audience, List<Group> group) {
        this.id = id;
        this.time = time;
        this.teacher = teacher;
        this.cathedra = cathedra;
        this.audience = audience;
        this.subject = subject;
        this.group = group;
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

    public List<Group> getGroup() {
        return group;
    }

    public void setGroup(List<Group> group) {
        this.group = group;
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
        return time.getStart_lection();
    }

    public LocalDateTime getLocalTimeStart() {
        return time.getStartLocal();
    }

    public String getTimeEnd() {
        return time.getEnd_lection();
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
        return Objects.equals(id, lecture.id) && Objects.equals(time, lecture.time) && Objects.equals(teacher, lecture.teacher) && Objects.equals(group, lecture.group) && Objects.equals(audience, lecture.audience) && Objects.equals(subject, lecture.subject) && Objects.equals(cathedra, lecture.cathedra);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, time, teacher, group, audience, subject, cathedra);
    }
}
