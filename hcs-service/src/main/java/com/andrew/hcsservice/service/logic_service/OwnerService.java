package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.model.dto.DocDTO;
import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.status.AmndStatus;
import com.andrew.hcsservice.repository.OwnerRepository;
import lombok.AllArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;

@Service
@AllArgsConstructor
public class OwnerService {
    private final OwnerRepository ownerRepository;

    public void addOwner(Owner owner){
        ownerRepository.save(owner);
    }

    public Owner mapDocOnOwner(DocDTO docDTO){
        ModelMapper mapper = new ModelMapper();
        Owner newOwner = mapper.map(docDTO, Owner.class);
        newOwner.setAmndDate(LocalDate.now());
        newOwner.setAmndState(AmndStatus.ACTIVE.getShortName());
        return newOwner;
    }

    public boolean isFindOwner(String email, String passport){
        return ownerRepository.findByEmailAndPassport(email, passport) == null;
    }
}
