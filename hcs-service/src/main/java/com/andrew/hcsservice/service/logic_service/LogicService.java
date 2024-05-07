package com.andrew.hcsservice.service.logic_service;

import com.andrew.hcsservice.exceptions.ResponseBody;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.Optional;

@RequiredArgsConstructor
@Service
public class LogicService<T> {
    private final JpaRepository<T, Long> repository;

    public ResponseEntity<?> createEntity(T data, Long id){
        Optional<T> optionalT = repository.findById(id);
        if(optionalT.isEmpty()){
            return ResponseEntity.badRequest()
                    .body(new ResponseBody<>(HttpStatus.BAD_REQUEST.value(), "Id incorrect!"));
        }
        T newT = optionalT.get();

        return null;
    }

    /*
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
     */

}
