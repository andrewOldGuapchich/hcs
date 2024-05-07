package com.andrew.hcsservice.config.controller;

import com.andrew.hcsservice.model.dto.DocDto;
import com.andrew.hcsservice.service.logic_service.DocService;
import lombok.AllArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/doc")
public class DocController {
    private final DocService docService;

    @PostMapping("/add-doc")
    public void add(@RequestBody DocDto docDTO){
        docService.addDoc(docDTO);
    }
}
