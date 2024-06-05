package com.andrew.hcsservice.controller;

import com.andrew.hcsservice.model.dto.doc.DocDto;
import com.andrew.hcsservice.service.service_interfaces.DocInterfaces;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@AllArgsConstructor
@RequestMapping("/doc")
public class DocController {
    private DocInterfaces docService;

    @Autowired
    public void setDocService(@Lazy DocInterfaces docService) {
        this.docService = docService;
    }

    @PostMapping("/add-doc-new")
    public ResponseEntity<?> addNewDoc(@RequestBody DocDto docDTO){
       return docService.addDocNew(docDTO);
    }

    /*@PostMapping("/add-doc-remove")
    public void addRemoveDoc(@RequestBody DocDto docDTO){
        docService.addDocRemove(docDTO);
    }*/
}
