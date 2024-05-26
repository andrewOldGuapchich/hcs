package com.andrew.hcsservice.model.dto.doc;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;

@Data
public class IdDto {
    private final List<Long> idList;

    @JsonCreator
    public IdDto(@JsonProperty("idList") List<Long> idList) {
        this.idList = idList;
    }
}
