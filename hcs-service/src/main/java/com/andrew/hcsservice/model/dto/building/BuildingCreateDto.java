package com.andrew.hcsservice.model.dto.building;

import lombok.Data;

@Data
public class BuildingCreateDto {
    private int roomCount;
    private int number;
    private Long idStreet;
    private String buildingStatus;
}
