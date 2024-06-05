package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.AppException;
import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.building.BuildingCreateDto;
import com.andrew.hcsservice.model.dto.building.InfoBuildingDto;
import com.andrew.hcsservice.model.entity.building.Building;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.entity.building.Street;
import com.andrew.hcsservice.model.status.AmndStatus;
import com.andrew.hcsservice.repository.building.BuildingRepository;
import com.andrew.hcsservice.repository.building.RoomSpaceRepository;
import com.andrew.hcsservice.repository.building.StreetRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final StreetService streetService;
    private final RoomSpaceRepository roomSpaceRepository;

    public ResponseEntity<?> getAll(){
        System.out.println("Service");
        List<Building> buildingList = buildingRepository.findAll();
        List<InfoBuildingDto> infoBuildingDto = new ArrayList<>();
        for (Building b : buildingList){
            InfoBuildingDto item = new InfoBuildingDto();
            item.setStreet(b.getStreet().getName());
            item.setNumber(b.getNumber());
            item.setId(b.getId());
            item.setRoomSpaceList(b.getRoomSpaceList());
            item.setCountRoomSpace(b.getRoomCount());
            infoBuildingDto.add(item);
        }
        return ResponseEntity.ok(infoBuildingDto);
    }

    public ResponseEntity<?> getBuildInfo(String nameStreet, String number){
        try{
                Street street = streetService.findByName(nameStreet);
                Building building = validateAndGetBuilding(
                        buildingRepository.findBuildingByStreetAndNumber(street, number));
                InfoBuildingDto item = new InfoBuildingDto();

                item.setStreet(building.getStreet().getName());
                item.setNumber(building.getNumber());
                item.setId(building.getId());
                item.setRoomSpaceList(building.getRoomSpaceList());
                item.setCountRoomSpace(building.getRoomCount());

                return ResponseEntity.ok()
                        .body(new ResponseBody<>(HttpStatus.OK.value(), item));
        } catch (AppException appException) {
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public ResponseEntity<?> getHistoryInfo(String streetName, String number) {
        Street street = streetService.findByName(streetName);
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(),
                        buildingRepository.findHistoryBuildingInfo(street.getId(), number)));
    }

    public ResponseEntity<?> createBuilding(BuildingCreateDto buildingCreateDto){
        Street street = streetService.findByName(buildingCreateDto.getStreetName());
        if(buildingRepository
                .findBuildingByStreetAndNumber(street, buildingCreateDto.getNumber())
                .isPresent()) {
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "The building already exists!"));
        }

        Building building = new Building();
        building.setRoomCount(buildingCreateDto.getRoomCount());
        building.setNumber(buildingCreateDto.getNumber());
        building.setBuildingStatus(buildingCreateDto.getBuildingStatus());
        building.setStreet(street);
        building.setAmndState(AmndStatus.ACTIVE.getShortName());
        building.setAmndDate(LocalDate.now());
        buildingRepository.save(building);

        System.out.println(building.getId());
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), building));
    }

    public ResponseEntity<?> updateBuilding(BuildingCreateDto updateBuildingDto, String streetName, String number){
        Street street = streetService.findByName(streetName);
        Optional<Building> optionalBuilding = buildingRepository.findBuildingByStreetAndNumber(street, number);
        try{
            Building oldBuilding = validateAndGetBuilding(optionalBuilding);
            oldBuilding.setAmndDate(LocalDate.now());
            oldBuilding.setAmndState(AmndStatus.INACTIVE.getShortName());

            Building newBuilding = new Building();
            newBuilding.setRoomCount(updateBuildingDto.getRoomCount());
            newBuilding.setNumber(updateBuildingDto.getNumber());
            newBuilding.setBuildingStatus(updateBuildingDto.getBuildingStatus());
            newBuilding.setStreet(oldBuilding.getStreet());
            newBuilding.setAmndState(AmndStatus.ACTIVE.getShortName());
            newBuilding.setAmndDate(LocalDate.now());
            newBuilding.setOidBuilding(oldBuilding);

            buildingRepository.save(oldBuilding);
            buildingRepository.save(newBuilding);

            List<RoomSpace> roomSpaceList = roomSpaceRepository.findByBuilding(oldBuilding);
            for(RoomSpace roomSpace : roomSpaceList){
                roomSpace.setBuilding(newBuilding);
            }

            roomSpaceRepository.saveAll(roomSpaceList);
            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), newBuilding));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public ResponseEntity<?> deleteBuilding(String streetName, String number){
        Street street = streetService.findByName(streetName);
        Optional<Building> optionalBuilding = buildingRepository.findBuildingByStreetAndNumber(street, number);
        try{
            Building deleteBuilding = validateAndGetBuilding(optionalBuilding);
            deleteBuilding.setAmndDate(LocalDate.now());
            deleteBuilding.setAmndState(AmndStatus.INACTIVE.getShortName());
            buildingRepository.save(deleteBuilding);

            Building newBuilding = getNewBuilding(deleteBuilding);
            buildingRepository.save(newBuilding);

            List<RoomSpace> roomSpaceList = roomSpaceRepository.findByBuilding(deleteBuilding);
            for(RoomSpace r : roomSpaceList){
                r.setBuilding(newBuilding);
            }
            roomSpaceRepository.saveAll(roomSpaceList);
            return ResponseEntity.ok()
                    .body(new ResponseBody<>(HttpStatus.OK.value(), newBuilding));
        } catch (AppException appException){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public Building findBuildingByStreetAndNumber(String streetName, String number){
        Street street = streetService.findByName(streetName);
        return validateAndGetBuilding(buildingRepository.findBuildingByStreetAndNumber(street, number));
    }

    private Building getNewBuilding(Building oldBuilding) {
        Building building = new Building();
        building.setRoomCount(oldBuilding.getRoomCount());
        building.setStreet(oldBuilding.getStreet());
        building.setBuildingStatus(oldBuilding.getBuildingStatus());
        building.setNumber(oldBuilding.getNumber());
        building.setAmndDate(LocalDate.now());
        building.setAmndState(AmndStatus.CLOSE.getShortName());

        List<RoomSpace> newRoomSpaces = new ArrayList<>();
        for (RoomSpace roomSpace : oldBuilding.getRoomSpaceList()) {
            roomSpace.setBuilding(building);
            newRoomSpaces.add(roomSpace);
        }

        building.setRoomSpaceList(newRoomSpaces);
        building.setOidBuilding(oldBuilding);
        return building;
    }

    private Building validateAndGetBuilding(Optional<Building> buildingOptional){
        if(buildingOptional.isEmpty()){
            throw new AppException("Building not found!");
        }
        Building building = buildingOptional.get();
        if(!building.getAmndState().equals(AmndStatus.ACTIVE.getShortName())){
            throw new AppException("Incorrect id!");
        }
        return building;
    }
}
