package com.andrew.hcsservice.model.entity;

import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;

import javax.persistence.*;
import java.time.LocalDate;

@Data
@Entity
@Table(name = "owner_room")
public class OwnerRoom {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amnd_date")
    private LocalDate amndDate;

    @Column(name = "amnd_state")
    private String amndState;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false,
            cascade = CascadeType.PERSIST
    )
    @JoinColumn(name = "owner_room_oid")
    @JsonIgnore
    private OwnerRoom oidOwnerRoom;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "owner_id")
    private Owner owner;

    @JsonIgnore
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "room_id")
    private RoomSpace roomSpace;
}
