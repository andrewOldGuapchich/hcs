package com.andrew.hcsservice.model.dto;

import lombok.Data;

import java.util.List;

@Data
public class AddReadingDto {
    private String serialNumber;
    private double value;
    private String period;
    /*private Address address;
    private List<Reading> reading;

    @Data
    public static class Address {
        private String street;
        private String building;
        private int roomSpace;
    }

    @Data
    public static class Reading {
        private String type;
        private String subtype;
        private String serialNumber;
        private double value;
        private String period;
    }
     */
}
