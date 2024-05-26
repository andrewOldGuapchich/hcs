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
    private final StreetRepository streetRepository;
    private final RoomSpaceRepository roomSpaceRepository;
    private final ModelMapper mapper;


    public ResponseEntity<?> getBuildInfo(Long idBuilding){
        try{
            Optional<Building> buildingOptional = buildingRepository.findById(idBuilding);
            Building building = validateAndGetBuilding(buildingOptional);
            InfoBuildingDto item = new InfoBuildingDto();

            item.setStreet(building.getStreet().getName());
            item.setId(building.getId());
            item.setRoomSpaceList(building.getRoomSpaceList());
            item.setCountRoomSpace(building.getRoomCount());
            return ResponseEntity.ok(item);
        } catch (AppException appException) {
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public ResponseEntity<?> getAll(){
        List<Building> buildingList = buildingRepository.findAll();
        List<InfoBuildingDto> infoBuildingDto = new ArrayList<>();
        for (Building b : buildingList){
            InfoBuildingDto item = new InfoBuildingDto();
            item.setStreet(b.getStreet().getName());
            item.setId(b.getId());
            item.setRoomSpaceList(b.getRoomSpaceList());
            item.setCountRoomSpace(b.getRoomCount());
            infoBuildingDto.add(item);
        }
        return ResponseEntity.ok(infoBuildingDto);
    }

    public ResponseEntity<?> getBuildInfo(String nameStreet, String number){
        try{
            Optional<Street> streetOptional = streetRepository.findStreetByName(nameStreet);
            if(streetOptional.isEmpty()){
                return  ResponseEntity.badRequest()
                        .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "Error! Street not found"));
            } else {
                Street street = streetOptional.get();
                Building building = validateAndGetBuilding(
                        buildingRepository.findBuildingByStreetAndNumber(street, number));
                InfoBuildingDto item = new InfoBuildingDto();

                item.setStreet(building.getStreet().getName());
                item.setId(building.getId());
                item.setRoomSpaceList(building.getRoomSpaceList());
                item.setCountRoomSpace(building.getRoomCount());
                return ResponseEntity.ok(item);
            }
        } catch (AppException appException) {
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), appException.getMessage()));
        }
    }

    public ResponseEntity<?> getHistoryInfo(Long idBuilding) {
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(),
                        buildingRepository.findHistoryBuildingInfo(idBuilding)));
    }

    public ResponseEntity<?> createBuilding(BuildingCreateDto buildingCreateDto){
        Optional<Street> optionalStreet = streetRepository.findById(buildingCreateDto.getIdStreet());
        if(optionalStreet.isEmpty())
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "Street not found!"));

        Street street = optionalStreet.get();
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
                //mapper.map(buildingCreateDto, Building.class);
        building.setStreet(street);
        building.setAmndState(AmndStatus.ACTIVE.getShortName());
        building.setAmndDate(LocalDate.now());
        buildingRepository.save(building);

        System.out.println(building.getId());
        return ResponseEntity.ok()
                .body(new ResponseBody<>(HttpStatus.OK.value(), building));
    }

    public ResponseEntity<?> updateBuilding(BuildingCreateDto updateBuildingDto, Long idBuilding){
        Optional<Building> optionalBuilding = buildingRepository.findById(idBuilding);
        try{
            Building oldBuilding = validateAndGetBuilding(optionalBuilding);
            oldBuilding.setAmndDate(LocalDate.now());
            oldBuilding.setAmndState(AmndStatus.INACTIVE.getShortName());
            oldBuilding.setId(idBuilding);

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

    public ResponseEntity<?> deleteBuilding(Long id){
        Optional<Building> buildingOptional = buildingRepository.findById(id);
        try{
            Building deleteBuilding = validateAndGetBuilding(buildingOptional);
            deleteBuilding.setId(id);
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

     private Building getNewBuilding(Building oldBuilding){
         Building building = new Building();
         building.setRoomCount(oldBuilding.getRoomCount());
         building.setStreet(oldBuilding.getStreet());
         building.setAmndDate(LocalDate.now());
         building.setAmndState(AmndStatus.CLOSE.getShortName());

        List<RoomSpace> newRoomSpaces = new ArrayList<>();
        for(RoomSpace roomSpace : oldBuilding.getRoomSpaceList()){
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
