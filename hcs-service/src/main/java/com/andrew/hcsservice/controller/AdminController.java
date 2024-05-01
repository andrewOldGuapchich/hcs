package com.andrew.hcsservice.controller;

import com.andrew.hcsservice.service.logic_service.AdminService;
import lombok.AllArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/admin")
public class AdminController {
    private final AdminService adminService;
    @GetMapping("/all")
    public ResponseEntity<?> getAllDoc(){
        return adminService.getDocWantedStatus();
    }
}
