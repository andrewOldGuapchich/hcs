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
@Table(name = "street")
public class Street {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "amnd_date")
    @JsonFormat(pattern = "yyyy-MM-dd")
    private LocalDate amndDate;

    @Column(name = "amnd_state")
    private String amndState;

    @Column(name = "_name")
    private String name;

    @Column(name = "building_count")
    private int countBuilding;

    @OneToOne(
            fetch = FetchType.LAZY,
            optional = false
    )
    @JoinColumn(name = "street_oid")
    @JsonIgnore
    private Street oidStreet;

    @OneToMany(
            mappedBy = "street",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Building> buildings;
}
