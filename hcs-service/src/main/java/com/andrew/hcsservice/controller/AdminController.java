package com.andrew.hcsservice.controller;

import com.andrew.hcsservice.model.dto.DocDto;
import com.andrew.hcsservice.model.dto.roomspace.RoomSpaceCreateDto;
import com.andrew.hcsservice.model.entity.status.RoomSpaceStatus;
import com.andrew.hcsservice.service.logic_service.AdminService;
import com.andrew.hcsservice.service.logic_service.RoomSpaceService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;

    @GetMapping("/all")
    public ResponseEntity<?> getAllDoc(){
        return adminService.getDocWantedStatus();
    }

    @PostMapping("/add-user")
    public ResponseEntity<?> addNewUser(@RequestBody DocDto docDTO){
        return adminService.registerNewOwner(docDTO);
    }


}
