package com.andrew.hcsservice.service.service_interfaces;

import com.andrew.hcsservice.model.dto.owner.InfoOwnerDto;
import com.andrew.hcsservice.model.dto.owner.OwnerDto;
import com.andrew.hcsservice.model.entity.doc.Doc;
import com.andrew.hcsservice.model.entity.Owner;
import org.springframework.http.ResponseEntity;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Optional;

public interface OwnerInterfaces {
    ResponseEntity<?> updateOwnerInfo(OwnerDto updateOwnerDto, Long ownerId);

    ResponseEntity<?> createDeleteDoc(Long ownerId);
    void deleteOwner(Long id);

    ResponseEntity<?> findOwnerInfo(Long idOwner);

    ResponseEntity<?> findOwnerInfo(String email, String passport);

    List<Owner> findAll();

    List<InfoOwnerDto> findAllWithRoomSpaces();

    Optional<Owner> findByEmailAndPassport(String email, String passport);

    LinkedHashMap<Long, String> mapAndRegisterOwner(List<Doc> docList);

    boolean isFindOwner(String email, String passport);



}
