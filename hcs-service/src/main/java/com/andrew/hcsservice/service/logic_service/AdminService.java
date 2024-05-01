package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.model.dto.DocDTO;
import com.andrew.hcsservice.model.entity.Doc;
import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.status.DocStatus;
import com.andrew.hcsservice.repository.DocRepository;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@AllArgsConstructor
public class AdminService {
    private final DocRepository docRepository;
    private final OwnerService ownerService;

    public ResponseEntity<?> getDocWantedStatus(){
        String status = DocStatus.WAITING.getShortName();
        System.out.println(docRepository.findByStatus(status).size());
        return ResponseEntity.ok().body(docRepository.findByStatus(status));
    }

    public ResponseEntity<?> registerNewOwner(DocDTO docDTO){
        Owner newOwner = ownerService.mapDocOnOwner(docDTO);
        if(ownerService.isFindOwner(newOwner.getEmail(), newOwner.getPassport())){
            return ResponseEntity.badRequest().body("Ошибка!");
        } else {
            ownerService.addOwner(newOwner);
            return ResponseEntity.ok().body(newOwner);
        }
    }
}
