package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.model.dto.DocDto;
import com.andrew.hcsservice.model.entity.Doc;
import com.andrew.hcsservice.model.entity.status.DocStatus;
import com.andrew.hcsservice.repository.DocRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class DocService {
    private final OwnerService ownerService;
    private final DocRepository docRepository;

    public ResponseEntity<?> addDoc(DocDto docDTO){
        if(!ownerService.isFindOwner(docDTO.getEmail(), docDTO.getPassport())){
            return ResponseEntity.badRequest().body("Такой пользователь уже зарегистрирован!");
        }
        else {
            ModelMapper mapper = new ModelMapper();
            Doc doc = mapper.map(docDTO, Doc.class);
            doc.setCreateDate(LocalDate.now());
            doc.setStatus(DocStatus.WAITING.getShortName());
            docRepository.save(doc);
            return ResponseEntity.ok(doc);
        }
    }
}
