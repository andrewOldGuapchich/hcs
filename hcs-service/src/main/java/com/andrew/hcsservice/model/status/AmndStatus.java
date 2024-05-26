package com.andrew.hcsservice.model.status;

import java.util.HashMap;
import java.util.Map;

public enum AmndStatus {
    ACTIVE("A"),
    INACTIVE("I"),
    CLOSE("C"),
    WAITING("W");

    private final String shortName;

    private static final Map<String, AmndStatus> shortNameToStatusMap = new HashMap<>();

    static {
        for (AmndStatus status : AmndStatus.values()) {
            shortNameToStatusMap.put(status.shortName, status);
        }
    }

    AmndStatus(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static AmndStatus fromShortName(String shortName) {
        return shortNameToStatusMap.get(shortName);
    }
}
