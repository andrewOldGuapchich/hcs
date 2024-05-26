package com.andrew.hcsservice.model.status;

import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;

public enum DocType {
    @JsonProperty("N")
    NEW("N"),
    @JsonProperty("R")
    REMOVE("R"),
    @JsonProperty("U")
    UPDATE("U");

    private final String shortName;

    private static final Map<String, DocType> shortNameToStatusMap = new HashMap<>();

    static {
        for (DocType type : DocType.values()) {
            shortNameToStatusMap.put(type.shortName, type);
        }
    }

    DocType(String shortName) {
        this.shortName = shortName;
    }

    public String getShortName() {
        return shortName;
    }

    public static DocType fromShortName(String shortName) {
        return shortNameToStatusMap.get(shortName);
    }
}
