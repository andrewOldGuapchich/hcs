package com.andrew.hcsservice.controller;

import com.andrew.hcsservice.model.dto.DocDTO;
import com.andrew.hcsservice.service.logic_service.AdminService;
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
    public ResponseEntity<?> addNewUser(@RequestBody DocDTO docDTO){
        return adminService.registerNewOwner(docDTO);
    }
}
