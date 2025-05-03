package org.example.univer.models;

import jakarta.persistence.*;
import java.io.Serializable;
import java.util.Objects;
@NamedQueries({
        @NamedQuery(
                name = "findAllAudiences",
                query = "FROM Audience"
        ),
        @NamedQuery(
                name = "countAudiencesByRoomNumber",
                query = "SELECT COUNT(*) FROM Audience WHERE roomNumber = :roomNumber"
        ),
        @NamedQuery(
                name = "countAllAudiences",
                query = "SELECT COUNT(a) FROM Audience a"
        ),
        @NamedQuery(
                name = "findAllAudiencesPaginated",
                query = "FROM Audience ORDER BY id"
        )
})
@Entity
@Table(name = "audience")
public class Audience implements Serializable {

    private static final long serialVersionUID = -82658307871733049L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(name = "room_number", unique = true)
    private Integer roomNumber;
    @Column(name = "capacity", nullable = false)
    private Integer capacity;

    public Audience(Long id, Integer room, Integer capacity) {
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
