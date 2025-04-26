package org.example.univer.models;

import java.io.Serializable;
import java.util.Objects;

public class Audience implements Serializable {

    private static final long serialVersionUID = 82658307871733049L;
    private Long id;
    private Integer roomNumber;
    private Integer capacity;

    public Audience(Long id, Integer room, Integer size) {
        this.id = id;
        this.roomNumber = room;
        this.capacity = capacity;
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
        return roomNumber;
    }

    public void setRoom(Integer room) {
        this.roomNumber = room;
    }

    public Integer getCapacity() {
        return capacity;
    }

    public void setCapacity(Integer size) {
        this.capacity = size;
    }

    public String getRoomString() {
        return roomNumber.toString();
    }

    public String getCapacityString() {
        return capacity.toString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Audience audience = (Audience) o;
        return Objects.equals(id, audience.id) && Objects.equals(roomNumber, audience.roomNumber) && Objects.equals(capacity, audience.capacity);
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, roomNumber, capacity);
    }
}
