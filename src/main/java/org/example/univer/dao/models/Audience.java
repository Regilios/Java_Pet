package org.example.univer.dao.models;

import java.io.Serializable;
import java.util.Objects;

public class Audience implements Serializable {

    private static final long serialVersionUID = 82658307871733049L;
    private Long id;
    private Integer room;
    private Integer size;

    public Audience(Long id, Integer room, Integer size) {
        this.id = id;
        this.room = room;
        this.size = size;
    }
    public Audience() {

    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Integer getRoom() {
        return room;
    }

    public void setRoom(Integer room) {
        this.room = room;
    }

    public Integer getSize() {
        return size;
    }

    public void setSize(Integer size) {
        this.size = size;
    }

    public String getRoomString() {
        return room.toString();
    }
    public String getSizeString() {
        return size.toString();
    }
    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audience audience = (Audience) o;
        return Objects.equals(id, audience.id) && Objects.equals(room, audience.room) && Objects.equals(size, audience.size);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, room, size);
    }


}
