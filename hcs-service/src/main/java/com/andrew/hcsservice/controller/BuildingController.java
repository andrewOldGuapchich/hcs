package com.andrew.hcsservice.controller;

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
        System.out.println("Build controller");
        ResponseEntity<?> response = buildingService.getAll();
        System.out.println("send controller");
        return response;
    }

    @GetMapping("/find-by-street-number")
    public ResponseEntity<?> getInfo(@RequestParam(name = "streetName") String nameStreet,
                                     @RequestParam(name = "number") String number) {
        return buildingService.getBuildInfo(nameStreet, number);
    }

    @GetMapping("/history-info")
    public ResponseEntity<?> getHistoryInfo(@RequestParam(name = "streetName") String streetName,
                                            @RequestParam(name = "number") String number) {
        return buildingService.getHistoryInfo(streetName, number);
    }

    @PostMapping("/create-building")
    public ResponseEntity<?> createStreet(@RequestBody BuildingCreateDto buildingCreateDto) {
        return buildingService.createBuilding(buildingCreateDto);
    }

    @PutMapping("/update-building")
    public ResponseEntity<?> updateStreet(@RequestBody BuildingCreateDto updateBuildingDto,
                                          @RequestParam(name = "streetName") String streetName,
                                          @RequestParam(name = "number") String number) {
        return buildingService.updateBuilding(updateBuildingDto, streetName, number);
    }

    @DeleteMapping("/delete-building")
    public ResponseEntity<?> deleteStreet(@RequestParam(name = "streetName") String streetName,
                                          @RequestParam(name = "number") String number){
        return buildingService.deleteBuilding(streetName, number);
    }
}