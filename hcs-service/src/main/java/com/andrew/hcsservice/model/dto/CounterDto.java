package com.andrew.hcsservice.model.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
public class CounterDto {
    private TypeData typeDate;
    private String serialNumber;

    @AllArgsConstructor
    @Data
    public static class TypeData{
        private String typeName;
        private String subtypeName;
    }
}


