package com.andrew.hcsservice.model.status;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public enum RoomSpaceStatus {
    @JsonProperty("R")
    RESIDENTIAL("R"),
    @JsonProperty("N")
    NOT_RESIDENTIAL("N"),
    @JsonProperty("C")
    COMMERCIAL("C");
    private final String shortName;

    private static final Map<String, RoomSpaceStatus> shortNameToStatusMap = new HashMap<>();

    static {
        for (RoomSpaceStatus status : RoomSpaceStatus.values()) {
            shortNameToStatusMap.put(status.shortName, status);
        }
    }

    RoomSpaceStatus(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static RoomSpaceStatus fromShortName(String shortName) {
        return shortNameToStatusMap.get(shortName);
    }
}
