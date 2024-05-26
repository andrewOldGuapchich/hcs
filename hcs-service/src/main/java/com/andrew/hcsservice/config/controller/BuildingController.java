package com.andrew.hcsservice.config.controller;

import com.andrew.hcsservice.model.dto.building.BuildingCreateDto;
import com.andrew.hcsservice.service.logic_service.BuildingService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/building")
public class BuildingController {
    private final BuildingService buildingService;

    @GetMapping("/find-all")
    public ResponseEntity<?> getAll() {
        return buildingService.getAll();
    }

    @GetMapping("/find-by-id")
    public ResponseEntity<?> getInfo(@RequestParam(name = "idBuilding") Long idBuilding) {
        return buildingService.getBuildInfo(idBuilding);
    }

    @GetMapping("/find-by-street-number")
    public ResponseEntity<?> getInfo(@RequestParam(name = "nameStreet") String nameStreet,
                                     @RequestParam(name = "number") String number) {
        return buildingService.getBuildInfo(nameStreet, number);
    }

    @GetMapping("/history-info")
    public ResponseEntity<?> getHistoryInfo(@RequestParam(name = "idBuilding") Long idBuilding) {
        return buildingService.getHistoryInfo(idBuilding);
    }

    @PostMapping("/create-building")
    public ResponseEntity<?> createStreet(@RequestBody BuildingCreateDto buildingCreateDto) {
        return buildingService.createBuilding(buildingCreateDto);
    }

    @PutMapping("/update-building")
    public ResponseEntity<?> updateStreet(@RequestBody BuildingCreateDto updateBuildingDto,
                                          @RequestParam(name = "idBuilding") Long idBuilding) {
        return buildingService.updateBuilding(updateBuildingDto, idBuilding);
    }

    @DeleteMapping("/delete-street")
    public ResponseEntity<?> deleteStreet(@RequestParam(name = "idBuilding") Long idBuilding){
        return buildingService.deleteBuilding(idBuilding);
    }
}