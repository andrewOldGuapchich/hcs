package com.andrew.hcsservice.model.entity.building;

import com.andrew.hcsservice.model.entity.OwnerRoom;
import com.andrew.hcsservice.model.entity.doc.UpdateDoc;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
@Entity
@Table(name = "room_space")
public class RoomSpace {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amnd_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate amndDate;

    @Column(name = "amnd_state")
    private String amndState;

    @Column(name = "_number")
    private int number;

    @Column(name = "total_area")
    private Double totalArea;

    @Column(name = "living_area")
    private Double livingArea;

    @Column(name = "status")
    private String status;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "room_oid")
    @JsonIgnore
    private RoomSpace oidRoomSpace;

    //добавить строение
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "building_id")
    @JsonIgnore
    private Building building;

    @JsonIgnore
    @OneToMany(mappedBy = "roomSpace",
            cascade = CascadeType.ALL,
            orphanRemoval = true)
    private List<OwnerRoom> ownerRoomList;

    @JsonIgnore
    @OneToMany(mappedBy = "roomSpace",
            cascade = CascadeType.ALL)
    private List<UpdateDoc> updateDocs;
}
