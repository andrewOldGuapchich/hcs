package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.OwnerRoom;
import com.andrew.hcsservice.repository.OwnerRoomRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class OwnerRoomSpaceService {
    private final OwnerRoomRepository ownerRoomRepository;

    public void saveAll(List<OwnerRoom> ownerRoomList){
        ownerRoomRepository.saveAll(ownerRoomList);
    }

    public List<OwnerRoom> findByOwner(Owner owner){
        return ownerRoomRepository.findByOwner(owner);
    }
}
