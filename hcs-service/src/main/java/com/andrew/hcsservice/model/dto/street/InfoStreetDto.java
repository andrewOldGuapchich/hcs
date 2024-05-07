package com.andrew.hcsservice.model.dto.street;

import com.andrew.hcsservice.model.entity.building.Building;
import lombok.Data;

import java.util.List;

@Data
public class InfoStreetDto {
    private Long id;
    private String name;
    private int countBuilding;
    private List<Building> buildings;
}
