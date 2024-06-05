package com.andrew.hcsservice.controller;

import com.andrew.hcsservice.model.dto.RoomOwnerDto;
import com.andrew.hcsservice.model.dto.doc.IdDto;
import com.andrew.hcsservice.service.logic_service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/admin")
public class AdminController {
    private AdminService adminService;
    @Autowired
    public void setAdminService(@Lazy AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/waiting-docs")
    public ResponseEntity<?> getAllDoc(@RequestParam(name = "type") String type){
        System.out.println("controller");
        return adminService.getWaitingStatus(type);
    }

    @PostMapping("/register-user")
    public ResponseEntity<?> registerNewUsers(@RequestBody IdDto idDto){
        return adminService.sendDocList(idDto.getIdList());
    }

    @GetMapping("/all")

    public ResponseEntity<?> all(){
        return adminService.findAllOwner();
    }

    @GetMapping("/all-and-room")
    public ResponseEntity<?> allWithRooms(){
        return adminService.findAllOwnerInfo();
    }

    @PostMapping("/posting-delete-doc/{id}")
    public ResponseEntity<?> postingDeleteDoc(@PathVariable("id") Long id){
        return adminService.postingDeleteDoc(id);
    }

    @PostMapping("/add-room-space")
    public ResponseEntity<?> addRoom (@RequestBody RoomOwnerDto roomOwnerDto){
        return adminService.addRoomForOwner(roomOwnerDto);
    }
}
