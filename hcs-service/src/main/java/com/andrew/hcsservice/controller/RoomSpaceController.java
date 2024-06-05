package com.andrew.hcsservice.controller;

import com.andrew.hcsservice.model.dto.CreateCounterDto;
import com.andrew.hcsservice.model.dto.roomspace.AddressRoomDto;
import com.andrew.hcsservice.model.dto.roomspace.RoomSpaceCreateDto;
import com.andrew.hcsservice.service.logic_service.RoomSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/room-space")
public class RoomSpaceController {
    private final RoomSpaceService roomSpaceService;
    @PostMapping("/add-room")
    public ResponseEntity<?> addRoom(@RequestBody RoomSpaceCreateDto roomSpaceCreateDto){
        return roomSpaceService.addNewRoomSpace(roomSpaceCreateDto);
    }

    @PostMapping("/update-room")
    public ResponseEntity<?> updateRoom(@RequestBody RoomSpaceCreateDto roomSpaceCreateDto){
        return roomSpaceService.updateRoomSpace(roomSpaceCreateDto);
    }

    @DeleteMapping("/delete-room")
    public ResponseEntity<?> delete(@RequestBody AddressRoomDto addressRoomDto){
        return roomSpaceService.deleteRoomSpace(addressRoomDto);
    }

    @GetMapping("/info")
    public ResponseEntity<?> find(@RequestParam("street") String street,
                                  @RequestParam("building") String building,
                                  @RequestParam("number") int number){
        AddressRoomDto addressRoomDto = new AddressRoomDto();
        addressRoomDto.setStreetName(street);
        addressRoomDto.setBuildNumber(building);
        addressRoomDto.setRoomNumber(number);
        return roomSpaceService.infoRoomSpaceWithCounter(addressRoomDto);
    }

    @PostMapping("/register-counters")
    public ResponseEntity<?> registerCounter(@RequestBody CreateCounterDto counterDto){
        return roomSpaceService.addCounterForRoomSpace(counterDto);
    }
}
