package com.andrew.hcsservice.model.dto;

import lombok.Data;

@Data
public class ReadingInfoDto {
    private String period;
    private String date;
    private double value;
}
