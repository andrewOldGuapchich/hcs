package com.andrew.hcsservice.model.dto.roomspace;

import com.andrew.hcsservice.model.entity.status.RoomSpaceStatus;
import lombok.Data;

@Data
public class InfoRoomSpaceDto {
    private int number;
    private double totalArea;
    private double livingArea;
}
