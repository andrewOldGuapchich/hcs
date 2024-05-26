package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.AppException;
import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.roomspace.RoomSpaceCreateDto;
import com.andrew.hcsservice.model.entity.Owner;
import com.andrew.hcsservice.model.entity.building.Building;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.status.AmndStatus;
import com.andrew.hcsservice.repository.building.BuildingRepository;
import com.andrew.hcsservice.repository.building.RoomSpaceRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class RoomSpaceService {
    private final RoomSpaceRepository roomSpaceRepository;
    private final BuildingRepository buildingRepository;

    @Transactional
    public ResponseEntity<?> addNewRoomSpace(RoomSpaceCreateDto roomSpaceCreateDto){
        Building building = buildingRepository.findById(roomSpaceCreateDto.getBuildingId()).orElse(null);
        if(building == null) {
            return ResponseEntity.badRequest().body("Не найден дом!");
        }
        else if (roomSpaceRepository.findByNumberAndBuilding(
                roomSpaceCreateDto.getNumber(), building).isPresent()){
            return ResponseEntity.badRequest().body("В этом доме уже зарегистрирована квартира с таким номером!");
        } else {
            RoomSpace roomSpace = mapperToRoomSpace(roomSpaceCreateDto);
            roomSpace.setAmndState(AmndStatus.ACTIVE.getShortName());
            roomSpace.setBuilding(building);

            roomSpaceRepository.save(roomSpace);
            return ResponseEntity.ok(roomSpace);
        }
    }

    @Transactional
    public ResponseEntity<?> updateRoomSpace(RoomSpaceCreateDto roomSpaceCreateDto, Long idRoomSpace) {
        try{
            Building building = buildingRepository.findById(roomSpaceCreateDto.getBuildingId()).orElse(null);
            if (building == null) {
                return ResponseEntity.badRequest()
                        .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "Не найден дом!"));
            }

            RoomSpace oldRoomSpace = validateAndGetRoomSpace(idRoomSpace);
            oldRoomSpace.setAmndState(AmndStatus.INACTIVE.getShortName());
            oldRoomSpace.setId(idRoomSpace);

            RoomSpace newRoomSpace = mapperToRoomSpace(roomSpaceCreateDto);
            newRoomSpace.setAmndState(AmndStatus.ACTIVE.getShortName());
            newRoomSpace.setOidRoomSpace(oldRoomSpace);
            newRoomSpace.setBuilding(building);

            roomSpaceRepository.save(oldRoomSpace);
            roomSpaceRepository.save(newRoomSpace);
            return ResponseEntity
                    .ok(new ResponseBody<>(HttpStatus.OK.value(), newRoomSpace));

        } catch (AppException e){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    @Transactional
    public ResponseEntity<?> deleteRoomSpace(Long idRoomSpace) {
        try{
            RoomSpace oldRoomSpace = validateAndGetRoomSpace(idRoomSpace);
            oldRoomSpace.setAmndState(AmndStatus.INACTIVE.getShortName());
            oldRoomSpace.setId(idRoomSpace);
            roomSpaceRepository.save(oldRoomSpace);

            RoomSpace newRoomSpace = mapperToRoomSpace(oldRoomSpace);
            newRoomSpace.setAmndState(AmndStatus.CLOSE.getShortName());
            roomSpaceRepository.save(newRoomSpace);

            return ResponseEntity
                    .ok(new ResponseBody<>(HttpStatus.OK.value(), newRoomSpace));
        } catch (AppException e){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), e.getMessage()));
        }
    }

    public List<RoomSpace> findRoomSpacesByOwner(Owner owner){
        return roomSpaceRepository.findByOwner(owner);
    }

    public List<RoomSpace> findListById(List<Long> ids){
        return roomSpaceRepository.findById(ids);
    }

    private RoomSpace mapperToRoomSpace(RoomSpace roomSpace){
        RoomSpace newRoomSpace = new RoomSpace();
        newRoomSpace.setNumber(roomSpace.getNumber());
        newRoomSpace.setBuilding(roomSpace.getBuilding());
        newRoomSpace.setAmndDate(LocalDate.now());
        newRoomSpace.setTotalArea(roomSpace.getTotalArea());
        newRoomSpace.setLivingArea(roomSpace.getLivingArea());
        newRoomSpace.setStatus(roomSpace.getStatus());
        newRoomSpace.setOidRoomSpace(roomSpace);
        return newRoomSpace;
    }

    private RoomSpace mapperToRoomSpace(RoomSpaceCreateDto roomSpaceCreateDto){
        RoomSpace roomSpace = new RoomSpace();
        roomSpace.setNumber(roomSpaceCreateDto.getNumber());
        roomSpace.setTotalArea(roomSpaceCreateDto.getTotalArea());
        roomSpace.setLivingArea(roomSpaceCreateDto.getLivingArea());
        roomSpace.setAmndDate(LocalDate.now());
        roomSpace.setStatus(roomSpaceCreateDto.getStatus().getShortName());
        return roomSpace;
    }

    private RoomSpace validateAndGetRoomSpace(Long idRoomSpace) {
        Optional<RoomSpace> roomSpaceOptional = roomSpaceRepository.findById(idRoomSpace);
        if (roomSpaceOptional.isEmpty()) {
            throw new AppException("Помещение не найдено!");
        }
        RoomSpace roomSpace = roomSpaceOptional.get();
        if (!roomSpace.getAmndState().equals(AmndStatus.ACTIVE.getShortName())) {
            throw new AppException("Помещение не найдено! Неверный id!");
        }
        return roomSpace;
    }
}
