package org.example.univer.models;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.Objects;

public class Student extends Person implements Serializable {
    private static final long serialVersionUID = -5326483353908974024L;
    private Long id;
    private Group group;

    public Student(Long id, String firstName, String lastName, Gender gender, String address, String email, String phone, LocalDate birthday, Group group) {
        super(firstName, lastName, gender, address, email, phone, birthday);
        this.id = id;
        this.group = group;
    }

    public Student() {
        super();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Group getGroup() {
        return group;
    }

    public String getGroupName() {
        return group.getName();
    }

    public void setGroup(Group group) {
        this.group = group;
    }



    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Student student = (Student) o;
        return Objects.equals(id, student.id) && Objects.equals(group, student.group);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), id, group);
    }
}
