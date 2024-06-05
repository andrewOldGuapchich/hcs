package com.andrew.hcsservice.model.dto.doc;

import lombok.Data;

import java.time.LocalDate;
@Data
public class DocDto {
    private OwnerName ownerName;
    private OwnerData ownerData;
    private OwnerInfo ownerInfo;

    @Data
    public static class OwnerName{
        private String ownerSurname;
        private String ownerName;
        private String ownerPatronymic;
    }

    @Data
    public static class OwnerData{
        private String passport;
        private LocalDate birthDate;
    }

    @Data
    public static class OwnerInfo{
        private String email;
        private String phoneNumber;
    }
}
