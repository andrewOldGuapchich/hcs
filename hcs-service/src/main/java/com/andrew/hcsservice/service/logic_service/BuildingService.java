package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.AppException;
import com.andrew.hcsservice.exceptions.ResponseBody;
import com.andrew.hcsservice.model.dto.building.BuildingCreateDto;
import com.andrew.hcsservice.model.entity.building.Building;
import com.andrew.hcsservice.model.entity.building.RoomSpace;
import com.andrew.hcsservice.model.entity.building.Street;
import com.andrew.hcsservice.model.entity.status.AmndStatus;
import com.andrew.hcsservice.repository.building.BuildingRepository;
import com.andrew.hcsservice.repository.building.RoomSpaceRepository;
import com.andrew.hcsservice.repository.building.StreetRepository;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.cglib.core.Local;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BuildingService {
    private final BuildingRepository buildingRepository;
    private final StreetRepository streetRepository;
    private final RoomSpaceRepository roomSpaceRepository;
    private final ModelMapper mapper;

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

        Building building = mapper.map(buildingCreateDto, Building.class);
        building.setStreet(street);
        building.setAmndState(AmndStatus.ACTIVE.getShortName());
        building.setAmndDate(LocalDate.now());

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

            Building newBuilding = mapper.map(updateBuildingDto, Building.class);
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
            Building building = validateAndGetBuilding(buildingOptional);

        }
    }

     /* private Street getNewStreet(Street oldStreet){
        Street newStreet = new Street();
        newStreet.setName(oldStreet.getName());
        newStreet.setAmndDate(LocalDate.now());
        newStreet.setCountBuilding(oldStreet.getCountBuilding());

        List<Building> newBuildingList = new ArrayList<>();
        for(Building building : oldStreet.getBuildings()){
            building.setStreet(newStreet);
            newBuildingList.add(building);
        }

        newStreet.setBuildings(newBuildingList);
        newStreet.setOidStreet(oldStreet);
        return newStreet;
    }*/

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
