package com.andrew.hcsservice.model.dto.doc;

import lombok.Data;

import java.time.LocalDate;
@Data
public class DocDto {
    private String ownerSurname;
    private String ownerName;
    private String ownerPatronymic;
    private String passport;
    private LocalDate birthDate;
    private String email;
    private String phoneNumber;
}