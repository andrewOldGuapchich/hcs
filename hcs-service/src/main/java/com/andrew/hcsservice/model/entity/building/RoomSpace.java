package com.andrew.hcsservice.model.entity.building;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;

import java.util.Date;

@Data
@Entity
@Table(name = "room_space")
public class RoomSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amnd_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private Date amndDate;

    @Column(name = "amnd_state")
    private String amndState;

    @Column(name = "_number")
    private String number;

    @Column(name = "total_area")
    private Double totalArea;

    @Column(name = "living_area")
    private Double livingArea;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "room_oid")
    @JsonIgnore
    private RoomSpace oidRoomSpace;

    //добавить строение
}
