package com.andrew.hcsservice.config.controller;

import com.andrew.hcsservice.model.dto.roomspace.RoomSpaceCreateDto;
import com.andrew.hcsservice.service.logic_service.RoomSpaceService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/flat")
public class FlatController {
    private final RoomSpaceService roomSpaceService;

    @PostMapping("/add-room")
    public ResponseEntity<?> addRoom(@RequestBody RoomSpaceCreateDto roomSpaceCreateDto){
        return roomSpaceService.addNewRoomSpace(roomSpaceCreateDto);
    }

    @PostMapping("/update-room/{id}")
    public ResponseEntity<?> updateRoom(@RequestBody RoomSpaceCreateDto roomSpaceCreateDto,
                                        @PathVariable Long id){
        return roomSpaceService.updateRoomSpace(roomSpaceCreateDto, id);
    }

    @DeleteMapping("/delete-room/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id){
        return roomSpaceService.deleteRoomSpace(id);
    }
}
