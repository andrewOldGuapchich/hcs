package com.andrew.hcsservice.model.dto.building;

import lombok.Data;

@Data
public class BuildingCreateDto {
    private int roomCount;
    private String number;
    private String streetName;
    private String buildingStatus;
}
