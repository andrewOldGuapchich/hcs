package com.andrew.hcsservice.model.dto.roomspace;

import com.andrew.hcsservice.model.status.RoomSpaceStatus;
import lombok.Data;

@Data
public class RoomSpaceCreateDto {
    private Address address;
    private Area area;
    private RoomSpaceStatus status;

    @Data
    public static class Address{
        private String streetName;
        private String buildNumber;
        private int number;
    }

    @Data
    public static class Area{
        private double totalArea;
        private double livingArea;
    }
}
