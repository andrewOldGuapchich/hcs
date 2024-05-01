package com.andrew.authserver.controller

import com.andrew.authserver.entity.dtos.RegistrationUserDto
import com.andrew.authserver.service.reg_serv.RegistrationService
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/registration")
open class RegistrationController(
        private var registrationService: RegistrationService
) {
    @PostMapping("/send-code")
    fun senMail(@RequestBody userDto: RegistrationUserDto){
        registrationService.sendEmailCode(userDto);
    }
}