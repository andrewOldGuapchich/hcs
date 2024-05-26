package com.andrew.hcsservice.config.controller;

import com.andrew.hcsservice.model.dto.owner.OwnerDto;
import com.andrew.hcsservice.service.logic_service.OwnerService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/owner")
@RequiredArgsConstructor
public class OwnerController {
    private final OwnerService ownerService;

    @PostMapping("/create-delete-doc")
    public ResponseEntity<?> deleteDocOwner(@RequestParam(name = "id") Long deleteId){
        return ownerService.createDeleteDoc(deleteId);
    }

    @PostMapping("/update-owner")
    public ResponseEntity<?> updateOwner(@RequestBody OwnerDto ownerDto,
                                         @RequestParam("id") Long id){
        return ownerService.updateOwnerInfo(ownerDto, id);
    }

    @GetMapping("/find-owner-info")
    public ResponseEntity<?> ownerInfo(@RequestParam("id") Long id){
        return ownerService.findOwnerInfo(id);
    }

    @GetMapping("/find-owner-info/{email}")
    public ResponseEntity<?> ownerInfo(@PathVariable("email") String email,
                                       @RequestParam("passport") String passport){
        return ownerService.findOwnerInfo(email, passport);
    }

    @PostMapping("/activate-user/{email}")
    private ResponseEntity<?> activateUser(@PathVariable("email") String email){
         return ownerService.activateOwner(email);
    }
}
