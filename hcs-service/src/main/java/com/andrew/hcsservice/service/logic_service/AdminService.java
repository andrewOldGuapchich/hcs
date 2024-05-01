package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.model.dto.DocDTO;
import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.status.AmndStatus;
import com.andrew.hcsservice.model.entity.status.DocStatus;
import com.andrew.hcsservice.repository.DocRepository;
import com.andrew.hcsservice.service.addit_service.EmailService;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;


@Service
@RequiredArgsConstructor
public class AdminService {
    private final DocRepository docRepository;
    private final OwnerService ownerService;
    private final EmailService emailService;

    public ResponseEntity<?> getDocWantedStatus(){
        String status = DocStatus.WAITING.getShortName();
        System.out.println(docRepository.findByStatus(status).size());
        return ResponseEntity.ok().body(docRepository.findByStatus(status));
    }

    public ResponseEntity<?> registerNewOwner(DocDTO docDTO){
        Owner newOwner = ownerService.mapDocOnOwner(docDTO);
        if(!ownerService.isFindOwner(newOwner.getEmail(), newOwner.getPassport())){
            return ResponseEntity.badRequest().body("Ошибка!");
        } else {
            newOwner.setAmndDate(LocalDate.now());
            newOwner.setAmndState(AmndStatus.INACTIVE.getShortName());
            ownerService.addOwner(newOwner);
            String url = "http://localhost:7170/registration/send-ver-code?email=" + docDTO.getEmail();
            emailService.sendEmail(docDTO.getEmail(), url);
            return ResponseEntity.ok().body(url);
        }
    }
}
