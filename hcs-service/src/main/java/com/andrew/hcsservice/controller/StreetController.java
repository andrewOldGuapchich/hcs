package com.andrew.hcsservice.controller;

import com.andrew.hcsservice.model.dto.street.CreateStreetDto;
import com.andrew.hcsservice.service.logic_service.StreetService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/street")
@RequiredArgsConstructor
public class StreetController {
    private final StreetService streetService;

    @GetMapping("/find-all")
    public ResponseEntity<?> getAll(){
        return streetService.getAll();
    }


    @GetMapping("/find-by-name")
    public ResponseEntity<?> getInfo(@RequestParam(name = "nameStreet") String nameStreet){
        return streetService.getStreetInfo(nameStreet);
    }

    @GetMapping("/history-info")
    public ResponseEntity<?> getHistoryInfo(@RequestParam(name = "idStreet") Long idStreet){
        return streetService.getHistoryInfo(idStreet);
    }

    @PostMapping("/create-street")
    public ResponseEntity<?> createStreet(@RequestBody CreateStreetDto createStreetDto){
        return streetService.createStreet(createStreetDto);
    }

    @PutMapping("/update-street")
    public ResponseEntity<?> updateStreet(@RequestBody CreateStreetDto updateStreetDto,
                                          @RequestParam String name){
        return streetService.updateStreet(updateStreetDto, name);
    }

    @DeleteMapping("/delete-street")
    public ResponseEntity<?> deleteStreet(@RequestParam String name){
        return streetService.deleteStreet(name);
    }
}
