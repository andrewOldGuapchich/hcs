package com.andrew.hcsservice.model.dto.building;

import com.andrew.hcsservice.model.entity.building.RoomSpace;
import lombok.Data;

import java.util.List;

@Data
public class InfoBuildingDto {
    private Long id;
    private String street;
    private String number;
    private int countRoomSpace;
    private List<RoomSpace> roomSpaceList;
}
