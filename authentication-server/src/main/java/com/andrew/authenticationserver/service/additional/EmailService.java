package com.andrew.authenticationserver.service.additional;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
@NoArgsConstructor
public class EmailService {
    @Value("${smtp.mail.name}")
    private String name;

    @Autowired
    private JavaMailSender sender;

    public void sendEmail(String toAddr, String subject, String text){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setFrom(name);
        message.setTo(toAddr);
        message.setSubject(subject);
        message.setText(text);

        sender.send(message);
    }
}

