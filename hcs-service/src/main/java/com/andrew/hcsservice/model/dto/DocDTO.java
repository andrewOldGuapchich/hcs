package com.andrew.hcsservice.model.dto;

import lombok.Data;

import java.time.LocalDate;
@Data
public class DocDTO {
    private String ownerSurname;
    private String ownerName;
    private String ownerPatronymic;
    private String passport;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
}
