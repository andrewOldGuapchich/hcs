package com.andrew.hcsservice.controller;

import com.andrew.hcsservice.model.dto.DocDTO;
import com.andrew.hcsservice.service.auth_service.DocService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
public class DocController {
    private final DocService docService;

    @PostMapping("/add-doc")
    public void add(@RequestBody DocDTO docDTO){
        docService.addDoc(docDTO);
    }
}
