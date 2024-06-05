package com.andrew.hcsservice.model.dto;

import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
public class CounterReadingDto {
    private CounterDto counterDto;
    private List<ReadingInfoDto> readings;
}
