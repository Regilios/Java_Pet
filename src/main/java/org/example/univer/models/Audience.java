package org.example.univer.models;

import jakarta.persistence.*;
import lombok.*;

import java.io.Serializable;

@Entity
@Getter
@Setter
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
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
}
