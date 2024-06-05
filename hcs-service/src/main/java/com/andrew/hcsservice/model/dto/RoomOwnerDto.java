package com.andrew.hcsservice.model.dto;

import lombok.Data;

@Data
public class RoomOwnerDto {
    private Owner owner;
    private Address address;

    @Data
    public static class Owner{
        private String ownerPassport;
        private String ownerEmail;
    }

    @Data
    public static class Address{
        private String streetName;
        private String buildNumber;
        private int roomNumber;
    }
}
