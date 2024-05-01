package com.andrew.authserver.service.reg_serv

import com.andrew.authserver.entity.dtos.RegistrationUserDto
import com.andrew.authserver.repository.RoleRepository
import com.andrew.authserver.repository.UserRepository
import com.andrew.authserver.service.add_serv.EmailService
import com.andrew.authserver.service.add_serv.JwtTokenService
import com.andrew.authserver.service.logic.UserService
import org.springframework.http.ResponseEntity
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder
import org.springframework.stereotype.Service
import java.util.Random
import com.andrew.authserver.entity.auth.User as User

@Service
open class RegistrationService (
        private final val userService: UserService,
        private final val emailService: EmailService
        ) {
    /*fun registrationUser(userDto: RegistrationUserDto): ResponseEntity<*>{
        if(userService.findByEmail(userDto.email).get().getCode() != userDto.code){
            return return ResponseEntity.badRequest().body("Код не верный!");
        } else {
            val user = userService.createNewUser(userDto);
            return ResponseEntity.ok("Пользователь зарегистрирован!" + user.getEmail());
        }
    }*/

    fun sendEmailCode(registrationUserDto: RegistrationUserDto): ResponseEntity<*> {
        if(!registrationUserDto.password.equals(registrationUserDto.confirmPassword)){
            return ResponseEntity.badRequest().body("Пароли не совпадают!");
        }
        if(userService.findByEmail(registrationUserDto.email).isPresent){
            return ResponseEntity.badRequest().body("Такой пользователь уже зарегистрирован!");
        }
        val code = generateCode();
        userService.updateVerificationCode(registrationUserDto.email, code);
        return ResponseEntity.ok("Send message" + sendPassword(registrationUserDto.email, code));
    }


    private fun sendPassword(email: String, code: Int) {
        val subject: String = "ЖКХ код";
        val text: String = "Code - $code";
        emailService.sendEmail(email, subject, text);
    }

    private fun generateCode(): Int{
        val random = Random();
        return random.nextInt(1000, 10000);
    }
}