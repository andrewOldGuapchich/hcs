package com.andrew.hcsservice.model.entity.status;


import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public enum DocStatus {
    @JsonProperty("W")
    WAITING("W"),
    POSTED("P");

    private final String shortName;

    private static final Map<String, DocStatus> shortNameToStatusMap = new HashMap<>();

    static {
        for (DocStatus status : DocStatus.values()) {
            shortNameToStatusMap.put(status.shortName, status);
        }
    }

    DocStatus(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static DocStatus fromShortName(String shortName) {
        return shortNameToStatusMap.get(shortName);
    }
}
