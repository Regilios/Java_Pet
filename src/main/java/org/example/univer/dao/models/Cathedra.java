package org.example.univer.dao.models;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Cathedra implements Serializable {
    private static final long serialVersionUID = -1911433105376550879L;
    private Long id;
    private String name;
    private List<Group> groups = new ArrayList<>();
    private List<Teacher> teachers = new ArrayList<>();
    private List<Lecture> leactions = new ArrayList<>();
    private List<Holiday> holydays = new ArrayList<>();

    public Cathedra() {}

    public Cathedra(Long id, String name, List<Group> groups, List<Teacher> teachers, List<Lecture> leactions, List<Holiday> holydays) {
        this.id = id;
        this.name = name;
        this.groups = groups;
        this.teachers = teachers;
        this.leactions = leactions;
        this.holydays = holydays;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Group> getGroups() {
        return groups;
    }

    public void setGroups(List<Group> groups) {
        this.groups = groups;
    }

    public List<Teacher> getTeachers() {
        return teachers;
    }

    public void setTeachers(List<Teacher> teachers) {
        this.teachers = teachers;
    }

    public List<Lecture> getLeactions() {
        return leactions;
    }

    public void setLeactions(List<Lecture> leactions) {
        this.leactions = leactions;
    }

    public List<Holiday> getHolydays() {
        return holydays;
    }

    public void setHolydays(List<Holiday> holydays) {
        this.holydays = holydays;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Cathedra cathedra = (Cathedra) o;
        return Objects.equals(id, cathedra.id) && Objects.equals(name, cathedra.name) && Objects.equals(groups, cathedra.groups) && Objects.equals(teachers, cathedra.teachers) && Objects.equals(leactions, cathedra.leactions) && Objects.equals(holydays, cathedra.holydays);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, name, groups, teachers, leactions, holydays);
    }
}
