package com.andrew.hcsservice.model.entity.building;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.Date;
import java.util.List;

@Data
@Entity
@Table(name = "building")
public class Building {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amnd_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate amndDate;

    @Column(name = "amnd_state")
    private String amndState;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "street_id")
    @JsonIgnore
    private Street street;

    @Column(name = "building_status")
    private String buildingStatus;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "building_oid")
    @JsonIgnore
    private Building oidBuilding;

    @Column(name = "count_room")
    private int roomCount;

    @Column(name = "_number")
    private String number;

    @OneToMany(mappedBy = "building",
            fetch = FetchType.LAZY)
    @JsonIgnore
    private List<RoomSpace> roomSpaceList;
}
