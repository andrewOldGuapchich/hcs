package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.OwnerRoom;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.status.AmndStatus;
import com.andrew.hcsservice.repository.OwnerRoomRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.sql.Struct;
import java.time.LocalDate;
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

    public List<OwnerRoom> findByRoomSpace(RoomSpace roomSpace){
        return ownerRoomRepository.findByRoomSpace(roomSpace);
    }

    public void addRoomSpaceForOwner(Owner owner, RoomSpace roomSpace) {
        OwnerRoom ownerRoom = new OwnerRoom();

        ownerRoom.setOwner(owner);
        ownerRoom.setRoomSpace(roomSpace);
        ownerRoom.setAmndDate(LocalDate.now());
        ownerRoom.setAmndState(AmndStatus.ACTIVE.getShortName());
        ownerRoomRepository.save(ownerRoom);
    }

    private boolean checkOwner(Owner owner, RoomSpace roomSpace){
        List<OwnerRoom> ownerRoomList = ownerRoomRepository.findByOwner(owner);
        boolean isCheck = false;
        for (OwnerRoom o : ownerRoomList){
            if(o.getRoomSpace().equals(roomSpace))
                return true;
        }
        return false;
    }
}
