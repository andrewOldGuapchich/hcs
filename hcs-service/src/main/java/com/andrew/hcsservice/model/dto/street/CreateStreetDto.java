package com.andrew.hcsservice.model.dto.street;

import lombok.Data;

@Data
public class CreateStreetDto {
    private String name;
    private int countBuilding;
}