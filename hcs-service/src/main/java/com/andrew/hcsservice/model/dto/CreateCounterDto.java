package com.andrew.hcsservice.model.dto;

import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class CreateCounterDto {
    private Address address;
    private List<TypeCounter> typeCounters;

    @Data
    public static class Address{
        private String street;
        private String building;
        private int roomSpace;
    }

    @Data
    public static class TypeCounter{
        private String type;
        private String subtype;
        private Date date;
    }

    @Data
    public static class Date {
        private LocalDate dateTo;
        private LocalDate dateFrom;
    }
}
