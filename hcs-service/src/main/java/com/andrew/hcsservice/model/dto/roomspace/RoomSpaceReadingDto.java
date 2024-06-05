package com.andrew.hcsservice.model.dto.roomspace;

import com.andrew.hcsservice.model.dto.CounterDto;
import com.andrew.hcsservice.model.status.RoomSpaceStatus;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.List;

@Data
public class RoomSpaceReadingDto {
    private StreetBuilding streetBuilding;
    private Area area;
    private List<CounterDto> counters;
    private RoomSpaceStatus status;

    @AllArgsConstructor
    @Data
    public static class StreetBuilding {
        private String streetName;
        private String buildName;
        private int number;
    }

    @AllArgsConstructor
    @Data
    public static class Area {
        private double totalArea;
        private double livingArea;
    }
}





