package com.andrew.hcsservice.model.dto.doc;

import com.andrew.hcsservice.model.entity.building.RoomSpace;
import lombok.Data;

import java.time.LocalDate;
import java.util.List;

@Data
public class UpdateDocDto {
    private String ownerSurname;
    private String ownerName;
    private String ownerPatronymic;
    private String passport;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
    private List<RoomSpace> roomSpaceList;
}
