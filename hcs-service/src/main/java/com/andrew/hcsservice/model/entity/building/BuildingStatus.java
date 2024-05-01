package com.andrew.hcsservice.model.entity.building;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table(name = "building_status")
public class BuildingStatus {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "_name")
    private String name;

    @OneToMany(
            mappedBy = "buildingStatus",
            fetch = FetchType.LAZY
    )
    @JsonIgnore
    private List<Building> buildings;
}
