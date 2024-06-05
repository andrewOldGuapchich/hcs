package com.andrew.hcsservice.model.dto.roomspace;

import lombok.Data;

@Data
public class AddressRoomDto {
    private String streetName;
    private String buildNumber;
    private int roomNumber;
}
