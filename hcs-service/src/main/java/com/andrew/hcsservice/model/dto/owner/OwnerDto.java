package com.andrew.hcsservice.model.dto.owner;

import com.andrew.hcsservice.model.dto.doc.DocDto;
import lombok.Data;

import java.time.LocalDate;

@Data
public class OwnerDto {
    private DocDto.OwnerName ownerName;
    private DocDto.OwnerData ownerData;
    private DocDto.OwnerInfo ownerInfo;

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
