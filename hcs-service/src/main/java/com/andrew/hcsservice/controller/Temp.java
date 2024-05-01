package com.andrew.hcsservice.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/user")
public class Temp {
    @GetMapping("/info")
    public String get(){
        return "info";
    }
}
